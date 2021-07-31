package io.github.eh.eh

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val currentAnimations:HashMap<Int,Pair<TranslateAnimation,ValueAnimator>> = HashMap()
    private val barArray = arrayOf(
    R.id.bar_1,
    R.id.bar_2,
    R.id.bar_3,
    R.id.bar_4,
    R.id.bar_5,
    R.id.bar_6,
    R.id.bar_7,
    R.id.bar_8,
    R.id.bar_9,
    R.id.bar_10,
    R.id.bar_11
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_startMatching.setOnClickListener {
            startAnimation()
        }
    }
    private fun startAnimation(){
        for (id in barArray) {
            var random = Random()
            var tAnim: TranslateAnimation = if(Math.random() > 0.5){
                TranslateAnimation(50.0f,-50.0f,0.0f,0.0f)
                //TranslateAnimation(-((random.nextInt(4) + 6)*10).toFloat(),((random.nextInt(4) + 6)*10).toFloat(),0.0f,0.0f)
            }else{
                TranslateAnimation(-50.0f,50.0f,0.0f,0.0f)
                //TranslateAnimation(((random.nextInt(4) + 6)*10).toFloat(),-((random.nextInt(4) + 6)*10).toFloat(),0.0f,0.0f)
            }
            tAnim.duration = ((random.nextInt(2)+1)*1000).toLong()
            tAnim.repeatCount = TranslateAnimation.INFINITE
            tAnim.repeatMode = TranslateAnimation.REVERSE
            tAnim.startOffset = ((random.nextInt(1)+Math.random())*1000).toLong()
            var view = findViewById<View>(id)
            view.startAnimation(tAnim)
            val colorAnimation =
                ValueAnimator.ofArgb(Color.parseColor("#f8b547"), Color.parseColor("#fde8c6"))
                    .apply {
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        duration = 2000
                        startDelay = (random.nextInt(2) * 1000).toLong()
                        addUpdateListener {
                            view.background.setTint(it.animatedValue as Int)
                        }
                    }
            colorAnimation.start()
            currentAnimations[id] = Pair<TranslateAnimation,ValueAnimator>(tAnim,colorAnimation)
        }
    }
    private fun stopAnimation(){
        currentAnimations.forEach {
            it.value.first.cancel()
            it.value.second.cancel()
        }
    }
}