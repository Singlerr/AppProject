package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window){
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_register)
        btn_moveToVerification.setOnClickListener {
            var intent = Intent(this@RegisterActivity,VerificationActivity::class.java)
            var user = User()
            var bundle = Bundle()
            bundle.putSerializable("user",user)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}