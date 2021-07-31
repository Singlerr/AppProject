package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_setting.*

class ProfileSettingActivity : AppCompatActivity() {
    private val GET_GALLERY_IMAGE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
        }
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
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result!!.resultCode == RESULT_OK && result!!.data != null && result!!.data!!.data != null) {
                    var uri = result!!.data!!.data
                    img_profileSettingImage.setImageURI(uri)
                }
            }
            launcher.launch(intent)
        }
    }
}