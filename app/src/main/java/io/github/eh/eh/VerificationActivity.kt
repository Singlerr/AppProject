package io.github.eh.eh

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_verification.*
import java.util.*

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification);
        var user = savedInstanceState!!.getSerializable("user") as User
        btn_reRequest.setOnClickListener {
            val http = HTTPBootstrap.builder()
                .host(Env.AUTH_API_URL)
                .port(1300)
                .streamHandler(object : StreamHandler{
                    override fun onRead(obj: Any?) {
                        if(obj is ResponseBundle){
                            if(obj.responseCode in 200..299){
                                if(obj.response == "SUCCESS_TRANSACTION"){
                                    var timer = object : CountDownTimer(Env.VERIFICATION_TIME_OUT.toLong(),1000){
                                        override fun onTick(p0: Long) {
                                            TODO("Not yet implemented")
                                            
                                        }
                                        override fun onFinish() {
                                            TODO("Not yet implemented")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onWrite(outputStream: HTTPContext?) {
                        outputStream!!.write(user)
                    }

                    override fun onFailed() {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}