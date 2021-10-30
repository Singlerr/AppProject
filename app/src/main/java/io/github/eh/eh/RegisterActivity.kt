package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.RequestBundle
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_register)
        var user = User()
        btn_previousPage.setOnClickListener {
            finish()
        }
        btn_moveToVerification.setOnClickListener {
            var phoneNumber = etv_registerPhoneNumber.text.toString()
            if (!phoneNumber.matches(Regex("\\d{11}"))) {
                var dialog = IAlertDialog.Builder(this)
                    .message("휴대폰 번호 형식에 맞춰 입력해주세요(예: 01012345678)")
                    .title("확인")
                    .positiveButton(
                        "확인"
                    ) { dialog, _ -> dialog!!.dismiss() }.create()
                dialog.show()
                return@setOnClickListener
            }
            var http = HTTPBootstrap.builder()
                .host(Env.REQ_AUTH_CODE_API)
                .port(Env.HTTP_PORT)
                .streamHandler(object : StreamHandler {
                    override fun onWrite(outputStream: HTTPContext) {
                        var reqBundle = RequestBundle()
                        user.phoneNumber = phoneNumber
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
    }
}