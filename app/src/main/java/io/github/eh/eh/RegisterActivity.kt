package io.github.eh.eh

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Debug
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_register)
        var user:User? = null
        btn_moveToVerification.setOnClickListener {
            var phoneNumber = etv_registerPhoneNumber.text.toString()

            if(! phoneNumber.matches(Regex("\\d{11}"))){
                var dialog = IAlertDialog.Builder(this)
                    .message("휴대폰 번호 형식에 맞춰 입력해주세요(예: 01012345678)")
                    .title("확인")
                    .positiveButton("확인"){
                    }.create()
                dialog.show()
                return@setOnClickListener
            }
            var http = HTTPBootstrap.builder()
                .host(Env.AUTH_REQ_API_URL)
                .port(Env.HTTP_PORT)
                .streamHandler(object : StreamHandler {
                    override fun onWrite(outputStream: HTTPContext) {
                        user = User()
                        user!!.phoneNumber = phoneNumber
                        outputStream.write(user)
                    }

                    override fun onRead(obj: Any?) {
                        if(obj is ResponseBundle){
                            if(obj.responseCode == 200){
                                var intent = Intent(this@RegisterActivity, VerificationActivity::class.java)
                                var bundle = Bundle()
                                bundle.putSerializable("user", user)
                                intent.putExtras(bundle)
                                startActivity(intent)
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
}