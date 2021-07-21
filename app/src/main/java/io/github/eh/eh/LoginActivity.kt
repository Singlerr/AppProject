package io.github.eh.eh

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // login button
        btn_login.setOnClickListener {
            val id = etv_id.text.toString()
            val pw = etv_password.text.toString()

            val bootstrap: HTTPBootstrap = HTTPBootstrap.builder()
                .port(1300)
                .host("testHost")
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
                                loginfailed()
                            }
                        }
                        else {
                            loginfailed()
                        }
                    }
                }).build()
            bootstrap.submit()
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
    private fun IntentSupport(user: Any?) {
        user as Bundle
        val tomainintent = Intent(this, MainActivity::class.java)
        intent.putExtras(user)
        startActivity(tomainintent)
    }

    //Toast message when login failed
    private fun loginfailed() {
        Toast.makeText(this, "로그인 실패, 아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
    }
}


