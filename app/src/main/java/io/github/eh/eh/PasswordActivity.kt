package io.github.eh.eh

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.Utils
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        var passwordRegex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#\$%^&*()+|=])[A-Za-z\\d~!@#\$%^&*()+|=]{8,16}\$")

        btn_pw_previousPage.setOnClickListener {
            finish()
        }

        etv_profileSettingPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(it: Editable?) {
                if (!it!!.toString().matches(passwordRegex)) {
                    passwordErrorMsg.text =
                        "비밀번호는 숫자, 문자, 특수문자(~!@#$%^&*()+)을 포함하여 8~16자 이내로 만들어야합니다."
                } else {
                    passwordErrorMsg.text = ""
                }
            }

        })

        etv_profileSettingPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(it: Editable?) {
                if (etv_profileSettingPassword.text.toString() != it!!.toString()) {
                    passwordErrorMsg.text = "비밀번호가 일치하지 않습니다."
                } else {
                    passwordErrorMsg.text = ""
                }
            }

        })
        btn_moveToInterestSetting.setOnClickListener {
            if (!etv_profileSettingPassword.text.toString().matches(passwordRegex)) {
                passwordErrorMsg.text = "비밀번호는 숫자, 문자, 특수문자(~!@#$%^&*()+)을 포함하여 8~16자 이내로 만들어야합니다."
                return@setOnClickListener
            }
            if (etv_profileSettingPasswordConfirm.text.toString() != etv_profileSettingPassword.text.toString()) {
                passwordErrorMsg.text = "비밀번호가 일치하지 않습니다."
                return@setOnClickListener
            }
            var user = Utils.getUser(intent)
            user!!.password = etv_profileSettingPassword.text.toString()

            var intent = Intent(this@PasswordActivity, InterestListActivity::class.java)

            Utils.setEssentialData(intent, user, this::class.java.name)
            startActivity(intent)
        }

    }
}