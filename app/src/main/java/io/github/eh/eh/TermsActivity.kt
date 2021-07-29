package io.github.eh.eh

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window){
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_terms)
    }
}