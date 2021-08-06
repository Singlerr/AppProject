package io.github.eh.eh

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.http.bundle.VerificationBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class VerificationActivity : AppCompatActivity() {
    private lateinit var user:User
    private var timer:CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_verification);
        user = intent.extras!!.getSerializable("user") as User
        etv_verificationPhoneNumber.setText(user.phoneNumber,TextView.BufferType.EDITABLE)
        startTimer()

        btn_reRequest.setOnClickListener {
            resetTimer()
            var http = HTTPBootstrap.builder()
                .host(Env.AUTH_REQ_API_URL)
                .port(Env.HTTP_PORT)
                .streamHandler(object : StreamHandler {
                    override fun onWrite(outputStream: HTTPContext) {
                        outputStream.write(user)

                    }
                    override fun onRead(obj: Any?) {
                        if(obj is ResponseBundle){
                            if(obj.responseCode != 200){
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
            if(! code.matches(Regex("\\d{5}"))){
                var dialog = IAlertDialog.Builder(this)
                    .message("인증번호 형식에 맞춰 입력해주세요(예: 12345)")
                    .title("확인")
                    .positiveButton("확인"){
                    }.create()
                dialog.show()
                return@setOnClickListener
            }

            var http = HTTPBootstrap.builder()
                .port(Env.HTTP_PORT)
                .host(Env.AUTH_CHK_API_URL)
                .streamHandler(object : StreamHandler{
                    override fun onWrite(outputStream: HTTPContext) {
                        var bundle = VerificationBundle(user.phoneNumber!!,code)
                        outputStream.write(bundle)
                    }

                    override fun onRead(obj: Any?) {
                        if(obj is ResponseBundle){
                            if(obj.responseCode == 200){
                                CoroutineScope(Dispatchers.Main).launch {
                                    var dialog = IAlertDialog.Builder(this@VerificationActivity)
                                        .title("확인")
                                        .message("인증이 완료되었습니다.")
                                        .positiveButton("확인"){
                                            var intent = Intent(this@VerificationActivity, ProfileSettingActivity::class.java)
                                            var bundle = Bundle()
                                            bundle.putSerializable("className", this::class.qualifiedName)
                                            var userBundle = Bundle()
                                            userBundle.putSerializable("user", user)
                                            intent.putExtra("classInfo", bundle)
                                            intent.putExtra("user", userBundle)
                                            startActivity(intent)
                                        }.create()
                                    dialog.show()
                                }
                            }else{
                                Log.e("Error",obj.response)
                            }
                        }
                    }

                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                http.submit()
            }

        }
    }
    private fun resetTimer(){
        timer = object :
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
        timeLeft.text = ""
    }
    private fun startTimer(){
        if(timer == null){
            timer = object :
                CountDownTimer(Env.VERIFICATION_TIME_OUT.toLong(), 1000) {
                override fun onTick(p0: Long) {
                    var second = p0 / 1000
                    var date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalTime.ofSecondOfDay(second)
                    } else {
                        second
                    }
                    Log.e("Error",second.toString())
                    Log.e("Errr",date.toString())
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
                }
            }
            timer!!.start()
        }else{
            timer!!.start()
        }
    }
    private fun stopTimer(){
        if(timer != null)
            timer!!.cancel()
    }
}