package io.github.eh.eh

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.RequestBundle
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.http.bundle.VerificationBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.*

class VerificationActivity : AppCompatActivity() {
    private lateinit var user: User
    private var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_verification)
        user = Utils.getUser(intent)!!
        etv_verificationPhoneNumber.setText(user.phoneNumber, TextView.BufferType.EDITABLE)
        startTimer()

        btn_reRequest.setOnClickListener {
            resetTimer()
            var http = HTTPBootstrap.builder()
                .host(Env.REQ_AUTH_CODE_API)
                .port(Env.HTTP_PORT)
                .streamHandler(object : StreamHandler {
                    override fun onWrite(outputStream: HTTPContext) {
                        var reqBundle = RequestBundle()
                        reqBundle.setMessage(user)
                        outputStream.write(reqBundle)
                    }

                    override fun onRead(obj: Any?) {
                        if (obj is ResponseBundle) {
                            if (obj.responseCode == 200) {
                                var intent =
                                    Intent(applicationContext, VerificationActivity::class.java)
                                Utils.setEssentialData(intent, user, this::class.java.name)
                                startActivity(intent)
                            } else {
                                CoroutineScope(Dispatchers.Main).launch {
                                    var dialog = IAlertDialog.Builder(applicationContext)
                                        .title("확인")
                                        .message("인증에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                                        .positiveButton("확인") { _, _ -> finish() }.create()
                                    dialog.show()
                                }
                            }
                        }
                    }
                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                http.submit()
            }
        }
        btn_moveToProfileSetting.setOnClickListener {
            var code = etv_verificationCode.text.toString()
            if (!code.matches(Regex("\\d{5}"))) {
                var dialog = IAlertDialog.Builder(this)
                    .message("인증번호 형식에 맞춰 입력해주세요(예: 12345)")
                    .title("확인")
                    .positiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
                dialog.show()
                return@setOnClickListener
            }

            var http = HTTPBootstrap.builder()
                .port(Env.HTTP_PORT)
                .host(Env.AUTH_CHK_API_URL)
                .streamHandler(object : StreamHandler {
                    override fun onWrite(outputStream: HTTPContext) {
                        var bundle = VerificationBundle(user.phoneNumber!!, code)
                        outputStream.write(bundle)
                    }

                    override fun onRead(obj: Any?) {
                        if (obj is ResponseBundle) {
                            if (obj.responseCode == 200) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    var dialog = IAlertDialog.Builder(this@VerificationActivity)
                                        .title("확인")
                                        .message("인증이 완료되었습니다.")
                                        .positiveButton("확인") { _, _ ->
                                            var intent = Intent(
                                                this@VerificationActivity,
                                                ProfileSettingActivity::class.java
                                            )
                                            user.userId = user.phoneNumber
                                            Utils.setEssentialData(
                                                intent,
                                                user,
                                                this::class.java.name
                                            )
                                            startActivity(intent)
                                        }.create()
                                    dialog.show()
                                }
                            } else {
                                when {
                                    obj.response == "VERIFICATION_NOT_FOUND" -> {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            var dialog =
                                                IAlertDialog.Builder(this@VerificationActivity)
                                                    .title("확인")
                                                    .message("인증에 실패했습니다. 제한 시간 내에 인증을 완료해주세요.")
                                                    .positiveButton("확인") { dialog, _ ->
                                                        dialog.dismiss()
                                                        finish()
                                                    }.create()
                                            dialog.show()
                                        }
                                    }
                                    obj.response == "CODE_MISMATCH" -> {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            var dialog =
                                                IAlertDialog.Builder(this@VerificationActivity)
                                                    .title("확인")
                                                    .message("인증에 실패했습니다.")
                                                    .positiveButton("확인") { dialog, _ ->
                                                        dialog.dismiss()
                                                        finish()
                                                    }.create()
                                            dialog.show()
                                        }
                                    }
                                    else -> {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            var dialog =
                                                IAlertDialog.Builder(this@VerificationActivity)
                                                    .title("확인")
                                                    .message("인증에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                                                    .positiveButton("확인") { _, _ ->
                                                        finish()
                                                    }.create()
                                            dialog.show()
                                        }
                                    }
                                }

                            }
                        }
                    }

                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                http.submit()
            }

        }
    }

    private fun resetTimer() {
        timer = object :
            CountDownTimer(Env.VERIFICATION_TIME_OUT.toLong(), 1000) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTick(p0: Long) {
                var second = p0 / 1000
                var date = LocalTime.ofSecondOfDay(second)
                timeLeft.text = date.toString()

            }

            override fun onFinish() {
                var dialog = IAlertDialog.Builder(this@VerificationActivity)
                    .title("시간 초과")
                    .message("인증 시간이 초과되었습니다. 다시 시도하세요.")
                    .positiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                dialog.create().show()
                timeLeft.text = ""
                TODO("Not yet implemented")
            }
        }
        timeLeft.text = ""
    }

    private fun startTimer() {
        if (timer == null) {
            timer = object :
                CountDownTimer(Env.VERIFICATION_TIME_OUT.toLong(), 1000) {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onTick(p0: Long) {
                    var second = p0 / 1000
                    var date = LocalTime.ofSecondOfDay(second)
                    timeLeft.text = date.toString()

                }

                override fun onFinish() {
                    var dialog = IAlertDialog.Builder(this@VerificationActivity)
                        .title("시간 초과")
                        .message("인증 시간이 초과되었습니다. 다시 시도하세요.")
                        .positiveButton("확인") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                    dialog.create().show()
                    timeLeft.text = ""
                }
            }
            timer!!.start()
        } else {
            timer!!.start()
        }
    }

    private fun stopTimer() {
        if (timer != null)
            timer!!.cancel()
    }
}