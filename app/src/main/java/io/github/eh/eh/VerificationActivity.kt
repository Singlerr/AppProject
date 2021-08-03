package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_verification);
        var user = intent.extras!!.getSerializable("user") as User
        btn_reRequest.setOnClickListener {
            val http = HTTPBootstrap.builder()
                .host(Env.AUTH_API_URL)
                .port(1300)
                .streamHandler(object : StreamHandler {
                    override fun onRead(obj: Any?) {
                        if (obj is ResponseBundle) {
                            if (obj.responseCode in 200..299) {
                                if (obj.response == "SUCCESS_TRANSACTION") {
                                    var timer = object :
                                        CountDownTimer(Env.VERIFICATION_TIME_OUT.toLong(), 1000) {
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
        btn_moveToProfileSetting.setOnClickListener {
            var intent = Intent(this, ProfileSettingActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable("className", this::class.qualifiedName)
            var user = User()
            //나중에 휴대폰 번호 저장
            user.phoneNumber = ""
            var userBundle = Bundle()
            userBundle.putSerializable("user", user)
            intent.putExtra("classInfo", bundle)
            intent.putExtra("user", userBundle)
            startActivity(intent)
        }
    }
}