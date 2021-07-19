
package io.github.eh.eh

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import kotlinx.android.synthetic.main.activity_login.*
import io.github.eh.eh.FindPasswordActivity
import io.github.eh.eh.MainActivity
import io.github.eh.eh.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // 로그인 버튼
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

                    override fun onRead(obj: Any) {
                        if(obj is User) {
                            if(obj.result == "SUCCESS_TRANSACTION") {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            else if(obj.result == "ERROR_TRANSACTION") {
                                dialog("fail")
                            }
                        }
                        else {
                            dialog("fail")
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

    private fun Intent(streamHandler: StreamHandler, java: Class<MainActivity>): Intent? {
        return null
    }


    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("fail")) {
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }
    }
}
