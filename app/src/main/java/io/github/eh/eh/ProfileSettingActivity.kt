package io.github.eh.eh

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_setting.*

class ProfileSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_setting)
        btn_profileSettingMale.setOnClickListener {
            findViewById<Button>(R.id.btn_profileSettingFemale).setBackgroundResource(R.drawable.button_gray)
            findViewById<Button>(R.id.btn_profileSettingMale).setBackgroundResource(R.drawable.button_rounded_medium)
            //Set sex of user to male
        }
        btn_profileSettingFemale.setOnClickListener {
            findViewById<Button>(R.id.btn_profileSettingMale).setBackgroundResource(R.drawable.button_gray)
            findViewById<Button>(R.id.btn_profileSettingFemale).setBackgroundResource(R.drawable.button_rounded_medium)
            //Set sex of user to female
        }
        img_profileSettingImage.setOnClickListener {

        }
    }
}