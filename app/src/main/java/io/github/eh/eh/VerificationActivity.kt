package io.github.eh.eh

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class VerificationActivity : AppCompatActivity() {
    private lateinit var user:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_verification);
        user = intent.extras!!.getSerializable("user") as User
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
                                            var second = p0 / 1000
                                            var date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                LocalTime.ofSecondOfDay(second)
                                            } else {
                                                second
                                            }
                                            timeLeft.text = date.toString()

                                        }

                                        override fun onFinish() {
                                            var dialog = IAlertDialog.Builder(this@VerificationActivity)
                                                .title("시간 초과")
                                                .message("인증 시간이 초과되었습니다. 다시 시도하세요.")
                                                .positiveButton("확인"){
                                                }
                                            dialog.create().show()
                                            timeLeft.text = ""
                                            TODO("Not yet implemented")
                                        }
                                    }
                                    timer.start()
                                }else{
                                    var dialog = IAlertDialog.Builder(this@VerificationActivity)
                                        .title("오류")
                                        .message("오류가 발생했습니다. 나중에 다시 시도하세요.")
                                        .positiveButton("확인"){
                                        }
                                    dialog.create().show()
                                }
                            }
                        }
                    }

                    override fun onWrite(outputStream: HTTPContext?) {
                        outputStream!!.write(user)
                    }
                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                http.submit()
            }
        }
        btn_moveToProfileSetting.setOnClickListener {
            var intent = Intent(this, ProfileSettingActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable("className", this::class.qualifiedName)
            var userBundle = Bundle()
            userBundle.putSerializable("user", user)
            intent.putExtra("classInfo", bundle)
            intent.putExtra("user", userBundle)
            startActivity(intent)
        }
    }
}