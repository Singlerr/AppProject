package io.github.eh.eh


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.databinding.ActivityMainBinding
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_interest_list.*


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
        //이전 액티비티에서 그 액티비티의 클래스 이름을 가져옵니다. 반드시 모든 액티비티는 새로운 액티비티를 실행할 때 자신의 클래스 정보를 전달해야합니다.
        var lastActivityClass = Class.forName(Utils.getClassName(intent))

        //화면 넘기기(고쳐야됩니다)
        //val intent = Intent(this, SubActivity::class.java)
        val intent2 = Intent(this, lastActivityClass)
        //CheckBox Listener 등록을 간소화시켰습니다
        var foodLayout = findViewById<LinearLayout>(R.id.foods)
        for (i in 0..foodLayout.childCount) {
            if(foodLayout.getChildAt(i) !is LinearLayout)
                continue
            var childLayout = foodLayout.getChildAt(i) as LinearLayout
            Log.e("size",childLayout.childCount.toString())
            for(j in 0..childLayout.childCount){
                var elem = childLayout.getChildAt(i)
                if (elem is Button) {

                    elem.tag = false
                    elem.setOnClickListener {
                        var checked = if(elem.tag is Boolean) elem.tag as Boolean else false
                        setFood(elem.text.toString(),checked)
                        if(checked) {
                            elem.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
                            elem.tag = false
                        }else {
                            elem.setBackgroundResource(R.drawable.button_rounded_high)
                            elem.tag = true
                        }
                    }
                }
            }
        }
        var hobbyLayout = findViewById<LinearLayout>(R.id.hobbies)
        for (i in 0..hobbyLayout.childCount) {
            if(hobbyLayout.getChildAt(i) !is LinearLayout)
                continue
            var childLayout = hobbyLayout.getChildAt(i) as LinearLayout
            for(j in 0..childLayout.childCount){
                var elem = childLayout.getChildAt(i)
                if (elem is Button) {
                    elem.tag = false
                    elem.setOnClickListener {
                        var checked = if(elem.tag is Boolean) elem.tag as Boolean else false
                        setHobby(elem.text.toString(),checked)
                        if(checked) {
                            elem.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
                            elem.tag = false
                        }else {
                            elem.setBackgroundResource(R.drawable.button_rounded_high)
                            elem.tag = true
                        }
                    }
                }
            }
        }
        var placeLayout = findViewById<LinearLayout>(R.id.places)
        for (i in 0..placeLayout.childCount) {
            if(placeLayout.getChildAt(i) !is LinearLayout)
                continue
            var childLayout = placeLayout.getChildAt(i) as LinearLayout
            for(j in 0..childLayout.childCount){
                var elem = childLayout.getChildAt(i)
                if (elem is Button) {
                    elem.tag = false
                    elem.setOnClickListener {
                        var checked = if(elem.tag is Boolean) elem.tag as Boolean else false
                        setPlace(elem.text.toString(),checked)
                        if(checked) {
                            elem.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
                            elem.tag = false
                        }else {
                            elem.setBackgroundResource(R.drawable.button_rounded_high)
                            elem.tag = true
                        }
                    }
                }
            }
        }
        // binding.complete.setOnClickListener { startActivity(intent) }
        backspace.setOnClickListener { startActivity(intent2) }

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
