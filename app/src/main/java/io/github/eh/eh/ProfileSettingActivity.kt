package io.github.eh.eh

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.serverside.Sex
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_profile_setting.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


class ProfileSettingActivity : AppCompatActivity() {
    private var sex: Sex? = null
    private var imageUri: Uri? = null


    private var stateSex = 0
    private var stateImg = 0
    private var stateNickName = 0
    private var stateBirthDay = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result!!.resultCode == RESULT_OK && result.data != null && result.data!!.data != null) {
                imageUri = result.data!!.data
                img_profileSettingImage.setImageURI(imageUri)
                btn_changeImg.isInvisible = false
                //plusSign.isInvisible = true
                stateImg = 1
                checkConditions()
            }
        }
        var user = Utils.getUser(intent)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
            exitTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_profile_setting)
        btn_changeImg.isInvisible = true
        btn_profileSettingMale.setOnClickListener {
            findViewById<Button>(R.id.btn_profileSettingFemale).setBackgroundResource(R.drawable.button_rounded_medium_unclicked)
            findViewById<Button>(R.id.btn_profileSettingMale).setBackgroundResource(R.drawable.button_rounded_medium)
            //Set sex of user to male
            sex = Sex.MALE
            stateSex = 1
            checkConditions()
        }
        btn_profileSettingFemale.setOnClickListener {
            findViewById<Button>(R.id.btn_profileSettingMale).setBackgroundResource(R.drawable.button_rounded_medium_unclicked)
            findViewById<Button>(R.id.btn_profileSettingFemale).setBackgroundResource(R.drawable.button_rounded_medium)
            //Set sex of user to female
            sex = Sex.FEMALE
            stateSex = 1
            checkConditions()
        }
        img_profileSettingImage.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            launcher.launch(intent)
        }

        etv_profileSettingBirth.addTextChangedListener {
            if (it!!.toString().matches(Regex("\\d{8}"))) {
                stateBirthDay = 1
                etv_profileSettingBirth.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.textColor
                    )
                )
            } else {
                stateBirthDay = 0
                etv_profileSettingBirth.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_red_light
                    )
                )
            }
            checkConditions()
        }
        etv_profileSettingNickName.addTextChangedListener {
            stateNickName = if (it!!.toString().length in 1..8) {
                etv_profileSettingNickName.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.textColor
                    )
                )
                1
            } else {
                etv_profileSettingNickName.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_red_light
                    )
                )
                0
            }
            checkConditions()
        }
        btn_moveToPassword.setOnClickListener {
            if (checkConditions()) {
                if (etv_profileSettingName.text.isEmpty()) {
                    errorMsg.text = "이름을 입력해주세요."
                    return@setOnClickListener
                }
                if (etv_profileSettingNickName.text.isEmpty()) {
                    errorMsg.text = "닉네임을 입력해주세요."
                    return@setOnClickListener
                }
                if (!etv_profileSettingBirth.text.toString().matches(Regex("\\d{8}"))) {
                    errorMsg.text = "생년월일을 정확하게 입력해주세요. (예: 20020111)"
                    return@setOnClickListener
                }
                if (sex == null) {
                    errorMsg.text = "성별을 선택해주세요."
                    return@setOnClickListener
                }
                if (imageUri == null) {
                    errorMsg.text = "프로필 이미지를 선택해주세요."
                    return@setOnClickListener
                }
                if (!cb_profileSettings.isChecked) {
                    errorMsg.text = "약관에 동의해주세요."
                    return@setOnClickListener
                }

                val name = etv_profileSettingName.text.toString()
                val nickName = etv_profileSettingNickName.text.toString()
                val birthDay = etv_profileSettingBirth.text.toString()
                var todayDate = Calendar.getInstance()
                val thisYear = todayDate.get(Calendar.YEAR).toString().toInt()
                try {
                    LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("yyyyMMdd"))
                } catch (e: DateTimeParseException) {
                    errorMsg.text = "생년월일을 정확하게 입력해주세요."
                    return@setOnClickListener
                }
                val dateBirthDay =
                    LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("yyyyMMdd"))
                val birthYear: Int = dateBirthDay.year
                var age: Int = thisYear - birthYear + 1

                user!!.name = name
                user.nickName = nickName
                user.birthDay = dateBirthDay
                user.sex = sex.toString()
                user.age = age
                user.image = contentResolver.openInputStream(imageUri!!)!!.readBytes()
                var obj = JSONObject()
                obj.put("food", JSONArray())
                obj.put("hobby", JSONArray())
                obj.put("place", JSONArray())
                user.setInterests(obj)
                toInterestListIntent(user)
            }
        }

        btn_changeImg.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            launcher.launch(intent)
        }
        btn_previousPage.setOnClickListener {
            finish()
        }

    }

    private fun toInterestListIntent(user: User) {
        val toInterestListIntent = Intent(applicationContext, PasswordActivity::class.java)
        Utils.setEssentialData(toInterestListIntent, user, this::class.java.name)
        startActivity(toInterestListIntent)
    }

    private fun checkConditions(): Boolean {
        var stateSum = stateBirthDay + stateImg + stateNickName + stateSex
        return if (stateSum == 4) {
            btn_moveToPassword.setBackgroundResource(R.drawable.button_rounded_medium)
            true
        } else {
            btn_moveToPassword.setBackgroundResource(R.drawable.button_rounded_medium_disabled)
            false
        }
    }
}