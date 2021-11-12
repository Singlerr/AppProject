package io.github.eh.eh


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.HttpStatus
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.RequestBundle
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_interest_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InterestListActivity : AppCompatActivity() {

    private var user: User? = null

    //체크했을 때 체크박스를 문자열에 저장하기 위한 함수 생성
    private fun setFood(array1: String, isChecked: Boolean) {
        var parentJson = user!!.parseInterest!!
        var json = parentJson.getJSONArray("food")
        if (isChecked) {
            if (Utils.hasValue(json, array1))
                json.remove(Utils.indexOf(json, array1))
        } else {
            if (!Utils.hasValue(json, array1))
                json.put(array1)
        }
        user!!.setInterests(parentJson)
    }

    private fun setHobby(array1: String, isChecked: Boolean) {
        var parentJson = user!!.parseInterest!!
        var json = parentJson.getJSONArray("hobby")
        if (isChecked) {
            if (Utils.hasValue(json, array1))
                json.remove(Utils.indexOf(json, array1))
        } else {
            if (!Utils.hasValue(json, array1))
                json.put(array1)
        }
        user!!.setInterests(parentJson)
    }

    private fun setPlace(array1: String, isChecked: Boolean) {
        var parentJson = user!!.parseInterest!!
        var json = parentJson.getJSONArray("place")
        if (isChecked) {
            if (Utils.hasValue(json, array1))
                json.remove(Utils.indexOf(json, array1))
        } else {
            if (!Utils.hasValue(json, array1))
                json.put(array1)
        }
        user!!.setInterests(parentJson)
    }

    //체크박스 눌렀을 때 위의 함수들 실행하기
    //다른 액티비티에서 작업하다가 붙여넣기 해서 오류가 발생했습니다.


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_interest_list)
        user = Utils.getUser(intent)



        complete.setOnClickListener {
            var json = user!!.parseInterest!!
            if (json.getJSONArray("food").length() < 1) {
                var dialog = IAlertDialog.Builder(this@InterestListActivity)
                    .title("알림")
                    .message("'음식' 관심사 중 적어도 하나를 선턱해주세요!")
                    .positiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
                dialog.show()
                return@setOnClickListener
            }
            if (json.getJSONArray("place").length() < 1) {
                var dialog = IAlertDialog.Builder(this@InterestListActivity)
                    .title("알림")
                    .message("'장소' 관심사 중 적어도 하나를 선턱해주세요!")
                    .positiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
                dialog.show()
                return@setOnClickListener
            }
            if (json.getJSONArray("hobby").length() < 1) {
                var dialog = IAlertDialog.Builder(this@InterestListActivity)
                    .title("알림")
                    .message("'취미' 관심사 중 적어도 하나를 선턱해주세요!")
                    .positiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
                dialog.show()
                return@setOnClickListener
            }

            var bootstrap = HTTPBootstrap.builder()
                .host(Env.AUTH_REGISTER_API_URL)
                .port(Env.HTTP_PORT)
                .streamHandler(object : StreamHandler {
                    override fun onWrite(outputStream: HTTPContext) {
                        var req = RequestBundle()
                        req.setMessage(user)
                        outputStream.write(req)
                    }

                    override fun onRead(obj: Any?) {
                        if (obj is ResponseBundle) {
                            if (obj.responseCode == HttpStatus.SC_OK) {
                                user!!.password = obj.response
                                var intent =
                                    Intent(this@InterestListActivity, MainActivity::class.java)
                                Utils.setEssentialData(
                                    intent = intent,
                                    user = user!!,
                                    this@InterestListActivity::class.java.name
                                )
                                startActivity(intent)
                            } else {
                                if (obj.responseCode == HttpStatus.SC_NOT_ACCEPTABLE) {
                                    var dialog = IAlertDialog.Builder(this@InterestListActivity)
                                        .title("알림")
                                        .message("이미 가입되어 있는 사용자입니다.")
                                        .positiveButton("확인") { dialog, _ ->
                                            dialog.dismiss()
                                            var intent = Intent(
                                                this@InterestListActivity,
                                                LoginActivity::class.java
                                            )
                                            Utils.setEssentialData(
                                                intent = intent,
                                                user = User(),
                                                className = this@InterestListActivity::class.java.name
                                            )
                                            startActivity(intent)
                                        }.create()
                                    dialog.show()
                                } else if (obj.responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                                    var dialog = IAlertDialog.Builder(this@InterestListActivity)
                                        .title("알림")
                                        .message("가입 중 오류가 발생하였습니다. 잠시 후에 시도하세요.")
                                        .positiveButton("확인") { dialog, _ ->
                                            dialog.dismiss()
                                            var intent = Intent(
                                                this@InterestListActivity,
                                                LoginActivity::class.java
                                            )
                                            Utils.setEssentialData(
                                                intent = intent,
                                                user = User(),
                                                className = this@InterestListActivity::class.java.name
                                            )
                                            startActivity(intent)
                                        }.create()
                                    dialog.show()
                                }
                            }
                        }
                    }

                }).build()
            CoroutineScope(Dispatchers.IO).launch {
                bootstrap.submit()
            }
        }
        //CheckBox Listener 등록을 간소화시켰습니다
        var foodLayoutIds = arrayOf(R.id.foods_1, R.id.foods_2)
        var hobbyLayoutIds = arrayOf(R.id.hobbies_1, R.id.hobbies_2, R.id.hobbies_3)
        var placeLayoutIds = arrayOf(R.id.places_1, R.id.places_2)

        foodLayoutIds.forEach {
            var layout = findViewById<LinearLayout>(it)
            layout.forEach { elem ->
                if (elem is Button) {
                    elem.tag = false
                    elem.setOnClickListener {
                        var checked = if (elem.tag is Boolean) elem.tag as Boolean else false
                        setFood(elem.text.toString(), checked)
                        if (checked) {
                            elem.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
                            elem.tag = false
                        } else {
                            elem.setBackgroundResource(R.drawable.button_rounded_high)
                            elem.tag = true
                        }
                    }
                }
            }
        }
        hobbyLayoutIds.forEach {
            var layout = findViewById<LinearLayout>(it)
            layout.forEach { elem ->
                if (elem is Button) {
                    elem.tag = false
                    elem.setOnClickListener {
                        var checked = if (elem.tag is Boolean) elem.tag as Boolean else false
                        setHobby(elem.text.toString(), checked)
                        if (checked) {
                            elem.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
                            elem.tag = false
                        } else {
                            elem.setBackgroundResource(R.drawable.button_rounded_high)
                            elem.tag = true
                        }
                    }
                }
            }
        }
        placeLayoutIds.forEach {
            var layout = findViewById<LinearLayout>(it)
            layout.forEach { elem ->
                if (elem is Button) {
                    elem.tag = false
                    elem.setOnClickListener {
                        var checked = if (elem.tag is Boolean) elem.tag as Boolean else false
                        setPlace(elem.text.toString(), checked)
                        if (checked) {
                            elem.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
                            elem.tag = false
                        } else {
                            elem.setBackgroundResource(R.drawable.button_rounded_high)
                            elem.tag = true
                        }
                    }
                }
            }
        }
        // binding.complete.setOnClickListener { startActivity(intent) }
        backspace.setOnClickListener { finish() }

        //체크박스
        /**
        binding.checkHansik.setOnCheckedChangeListener(listener)
        binding.checkYangsik.setOnCheckedChangeListener(listener)
        binding.checkJunsik.setOnCheckedChangeListener(listener)
        binding.checkIlsik.setOnCheckedChangeListener(listener)
        binding.checkAsian.setOnCheckedChangeListener(listener)
        binding.checkDessert.setOnCheckedChangeListener(listener)
        binding.checkDrink.setOnCheckedChangeListener(listener)
        binding.checkVeg.setOnCheckedChangeListener(listener)
        binding.checkWorkout.setOnCheckedChangeListener(listener)
        binding.checkReading.setOnCheckedChangeListener(listener)
        binding.checkMusic.setOnCheckedChangeListener(listener)
        binding.checkMovie.setOnCheckedChangeListener(listener)
        binding.checkSwimming.setOnCheckedChangeListener(listener)
        binding.checkBicycle.setOnCheckedChangeListener(listener)
        binding.checkTennis.setOnCheckedChangeListener(listener)
        binding.checkSoccer.setOnCheckedChangeListener(listener)
        binding.checkMedi.setOnCheckedChangeListener(listener)
        binding.checkStudy.setOnCheckedChangeListener(listener)
        binding.checkPicture.setOnCheckedChangeListener(listener)
        binding.checkGolf.setOnCheckedChangeListener(listener)
        binding.checkKorea.setOnCheckedChangeListener(listener)
        binding.checkBri.setOnCheckedChangeListener(listener)
        binding.checkFra.setOnCheckedChangeListener(listener)
        binding.checkChi.setOnCheckedChangeListener(listener)
        binding.checkJap.setOnCheckedChangeListener(listener)
        binding.checkAmer.setOnCheckedChangeListener(listener)
        binding.checkAust.setOnCheckedChangeListener(listener)
        binding.checkHonk.setOnCheckedChangeListener(listener)

         **/


    }
}
