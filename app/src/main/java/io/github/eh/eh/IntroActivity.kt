package io.github.eh.eh

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Window
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import io.github.eh.eh.asutils.ScreenSizeClassifier
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.serverside.Sex
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_intro.*
import org.json.JSONArray
import org.json.JSONObject

class IntroActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide()
            exitTransition = Slide()
        }
        setContentView(R.layout.activity_intro)
        var layout = findViewById<ConstraintLayout>(R.id.introLayout)
        var screenSize = ScreenSizeClassifier.getScreenSizeType(resources.displayMetrics.densityDpi)
        //Unsupported screen size
        if (screenSize.id == -1) {
            layout.setBackgroundResource(ScreenSizeClassifier.ScreenSize.LDPI.id)
        } else {
            layout.setBackgroundResource(screenSize.id)
        }
        loadingBar.max = 5000
        var animator = ObjectAnimator.ofInt(loadingBar, "progress", 0, 5000)
        animator.duration = 1000
        animator.interpolator = AccelerateInterpolator()
        animator.addListener({
            startActivity()
        })

        animator.start()

    }

    private fun startActivity() {
        var intent = Intent(this, InterestListActivity::class.java)
        var user = User()
        user.phoneNumber = "01048728361"
        user.setSex(Sex.MALE)
        var obj = JSONObject()
        obj.put("food", JSONArray())
        obj.put("hobby", JSONArray())
        obj.put("place", JSONArray())
        user.setInterests(obj)
        Utils.setEssentialData(intent,user,this::class.qualifiedName!!)
        startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

}