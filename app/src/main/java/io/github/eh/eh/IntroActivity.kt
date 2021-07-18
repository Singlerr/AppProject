package io.github.eh.eh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.Sex
import io.github.eh.eh.serverside.User

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

    }

}