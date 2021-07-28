package io.github.eh.eh

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window){
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide()
        }
        setContentView(R.layout.activity_login)

        // login button
        btn_login.setOnClickListener {
            val id = etv_id.text.toString()
            val pw = etv_password.text.toString()

            val bootstrap: HTTPBootstrap = HTTPBootstrap.builder()
                .port(1300)
                .host(Env.API_URL)
                .streamHandler(object : StreamHandler {


                    override fun onWrite(outputStream: HTTPContext?) {
                        val user = User()
                        user.userId = id
                        user.password = pw
                        outputStream!!.write(user)
                    }

                    override fun onRead(obj: Any?) {
                        if (obj is User) {
                            if (obj.result == "SUCCESS_TRANSACTION") {
                                IntentSupport(obj)
                            }
                            else if (obj.result == "ERROR_TRANSACTION") {
                                loginFailed()
                            }
                        }
                        else {
                            loginFailed()
                        }
                    }

                    override fun onFailed() {

                    }
                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    bootstrap.submit()
                }catch (e:Exception){
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


    }
    //go to MainActivity
    private fun IntentSupport(user: User?) {
        val tomainintent = Intent(this, MainActivity::class.java)
        var bundle:Bundle = Bundle()
        bundle.putSerializable("user",user)
        tomainintent.putExtra("user",bundle)
        startActivity(tomainintent)
    }

    //Toast message when login failed
    private fun loginFailed() {
        msg_error.text = "비밀번호가 일치하지 않습니다."
        //Utils.showMessageBox(this,"로그인 실패","로그인 실패, 아이디와 비밀번호를 다시 확인해주세요.",AlertDialog.BUTTON_POSITIVE)
        //Toast.makeText(this, "로그인 실패, 아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
    }
    private fun loginFailedIO(){
        msg_error.text = "로그인을 할 수 없습니다."
    }
}


