package io.github.eh.eh

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Explode
import android.transition.Slide
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window){
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide()
        }
        setContentView(R.layout.activity_intro)

        loadingBar.max = 5000
        var animator = ObjectAnimator.ofInt(loadingBar,"progress",0,5000)
        animator.duration = 1000
        animator.interpolator = AccelerateInterpolator()
        animator.addListener({
            startActivity()
        })

        animator.start()

    }
    private fun startActivity(){
        startActivity(Intent(this,LoginActivity::class.java),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

}