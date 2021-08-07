package io.github.eh.eh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import io.github.eh.eh.serverside.Sex
import kotlinx.android.synthetic.main.activity_profile_setting.*

class ProfileSettingActivity : AppCompatActivity() {
    private val GET_GALLERY_IMAGE = 200
    private var sex:Sex? = null
    private var imageUri:Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result!!.resultCode == RESULT_OK && result.data != null && result.data!!.data != null) {
                imageUri = result.data!!.data
                img_profileSettingImage.setImageURI(imageUri)
                btn_changeImg.isInvisible = false
                plusSign.isInvisible = true
            }
        }
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_profile_setting)
        btn_changeImg.isInvisible = true
        btn_profileSettingMale.setOnClickListener {
            findViewById<Button>(R.id.btn_profileSettingFemale).setBackgroundResource(R.drawable.button_rounded_medium_unclicked)
            findViewById<Button>(R.id.btn_profileSettingMale).setBackgroundResource(R.drawable.button_rounded_medium)
            //Set sex of user to male
            sex = Sex.MALE
        }
        btn_profileSettingFemale.setOnClickListener {
            findViewById<Button>(R.id.btn_profileSettingMale).setBackgroundResource(R.drawable.button_rounded_medium_unclicked)
            findViewById<Button>(R.id.btn_profileSettingFemale).setBackgroundResource(R.drawable.button_rounded_medium)
            //Set sex of user to female
            sex = Sex.FEMALE
        }
        img_profileSettingImage.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            launcher.launch(intent)
        }
        btn_changeImg.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            launcher.launch(intent)
        }
        btn_moveToInterestSetting.setOnClickListener {
            if(etv_profileSettingNickName.text.isEmpty()){
                errorMsg.text = "닉네임을 입력해주세요."
                return@setOnClickListener
            }
            if(! etv_profileSettingBirth.text.toString().matches(Regex("\\d{8}"))){
                errorMsg.text = "생년월일을 정확하게 입력해주세요. (예: 20020111)"
                return@setOnClickListener
            }
            if(sex == null){
                errorMsg.text = "성별을 선택해주세요."
                return@setOnClickListener
            }
            if(imageUri == null){
                errorMsg.text = "프로필 이미지를 선택해주세요."
                return@setOnClickListener
            }
            var intent = Intent(this,InterestListActivity::class.java)
            startActivity(intent)
        }
    }
}