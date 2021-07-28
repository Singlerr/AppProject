package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        overridePendingTransition(R.anim.swipe,R.anim.swipe)
        var intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

}