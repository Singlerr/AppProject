package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.db.LoginDatabase
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private var instance: LoginActivity? = null
    private lateinit var databaseHelper: LoginDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_login)
        instance = this

        databaseHelper = LoginDatabase(this, "session", null, 1)
        if (databaseHelper.sessionExists()) {
            var cursor = databaseHelper.select()
            var id = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("id"))
            var password = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("password"))
            if (id != null && password != null) {
                val bootstrap: HTTPBootstrap = HTTPBootstrap.builder()
                    .port(1300)
                    .host(Env.API_URL)
                    .streamHandler(object : StreamHandler {
                        override fun onWrite(outputStream: HTTPContext) {
                            val user = User()
                            user.userId = id
                            user.password = password
                            outputStream.write(user)
                        }

                        override fun onRead(obj: Any?) {
                            if (obj is User) {
                                if (obj.result == "SUCCESS_TRANSACTION") {
                                    IntentSupport(obj)
                                } else if (obj.result == "ERROR_TRANSACTION") {
                                    loginFailed()
                                }
                            } else {
                                loginFailed()
                            }
                        }

                    }).build()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        bootstrap.submit()
                    } catch (e: Exception) {
                        loginFailedIO()
                    }
                }
            }
        }

        // login button
        btn_login.setOnClickListener {
            val id = etv_id.text.toString()
            val pw = etv_password.text.toString()
            val bootstrap: HTTPBootstrap = HTTPBootstrap.builder()
                .port(1300)
                .host(Env.API_URL)
                .streamHandler(object : StreamHandler {


                    override fun onWrite(outputStream: HTTPContext) {
                        val user = User()
                        user.userId = id
                        user.password = pw
                        outputStream.write(user)
                    }

                    override fun onRead(obj: Any?) {
                        if (obj is User) {
                            if (obj.result == "SUCCESS_TRANSACTION") {
                                databaseHelper.insertOrUpdate(obj.userId, obj.password)
                                IntentSupport(obj)
                            } else if (obj.result == "ERROR_TRANSACTION") {
                                loginFailed()
                            }
                        } else {
                            loginFailed()
                        }
                    }

                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    bootstrap.submit()
                } catch (e: Exception) {
                    loginFailedIO()
                }
            }
        }
        // go to RegisterActivity
        btn_moveToRegister.setOnClickListener {
            val toregisterintent = Intent(this, RegisterActivity::class.java)
            startActivity(toregisterintent)
        }

        // go to FindPassWordActivity
        btn_findPassword.setOnClickListener {
            val tofindpwintent = Intent(this, FindPasswordActivity::class.java)
            startActivity(tofindpwintent)
        }
        btn_previousPage.setOnClickListener {
            var dialog = IAlertDialog.Builder(instance)
                .message("정말로 앱을 종료하시겠습니까?")
                .title("확인")
                .positiveButton("종료") {
                    finishAndRemoveTask()
                    exitProcess(0)
                }
                .negativeButton("취소") {

                }.create()
            dialog.show()
        }

    }

    //go to MainActivity
    private fun IntentSupport(user: User?) {
        val tomainintent = Intent(this, MainActivity::class.java)
        var bundle: Bundle = Bundle()
        bundle.putSerializable("user", user)
        tomainintent.putExtra("user", bundle)
        startActivity(tomainintent)
    }

    //Toast message when login failed
    private fun loginFailed() {
        msg_error.text = "비밀번호가 일치하지 않습니다."
        //Utils.showMessageBox(this,"로그인 실패","로그인 실패, 아이디와 비밀번호를 다시 확인해주세요.",AlertDialog.BUTTON_POSITIVE)
        //Toast.makeText(this, "로그인 실패, 아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
    }

    private fun loginFailedIO() {
        msg_error.text = "로그인을 할 수 없습니다."
    }
}


