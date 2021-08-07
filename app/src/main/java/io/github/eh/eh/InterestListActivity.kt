package io.github.eh.eh


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
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


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val listener by lazy {
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            when (buttonView.id) {
                R.id.checkHansik -> setFood("한식", isChecked)
                R.id.checkYangsik -> setFood("양식", isChecked)
                R.id.checkJunsik -> setFood("중식", isChecked)
                R.id.checkIlsik -> setFood("일식", isChecked)
                R.id.checkAsian -> setFood("아시아음식", isChecked)
                R.id.checkDessert -> setFood("디저트", isChecked)
                R.id.checkDrink -> setFood("술", isChecked)
                R.id.checkVeg -> setFood("비건", isChecked)
                R.id.checkWorkout -> setHobby("헬스", isChecked)
                R.id.checkReading -> setHobby("독서", isChecked)
                R.id.checkMusic -> setHobby("음악", isChecked)
                R.id.checkMovie -> setHobby("영화", isChecked)
                R.id.checkSwimming -> setHobby("수영", isChecked)
                R.id.checkBicycle -> setHobby("자전거", isChecked)
                R.id.checkTennis -> setHobby("테니스", isChecked)
                R.id.checkSoccer -> setHobby("축구", isChecked)
                R.id.checkMedi -> setHobby("명상", isChecked)
                R.id.checkStudy -> setHobby("공부", isChecked)
                R.id.checkPicture -> setHobby("사진", isChecked)
                R.id.checkGolf -> setHobby("골프", isChecked)
                R.id.checkKorea -> setPlace("한국", isChecked)
                R.id.checkBri -> setPlace("영국", isChecked)
                R.id.checkFra -> setPlace("프랑스", isChecked)
                R.id.checkChi -> setPlace("중국", isChecked)
                R.id.checkJap -> setPlace("일본", isChecked)
                R.id.checkAmer -> setPlace("미국", isChecked)
                R.id.checkAust -> setPlace("호주", isChecked)
                R.id.checkHonk -> setPlace("홍콩", isChecked)
            }
        }
    }


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
        }
        setContentView(binding.root)
        var bundle = intent.getBundleExtra("user")!!
        var classInfo = intent.getBundleExtra("classInfo")
        user = bundle.getSerializable("user") as User

        //이전 액티비티에서 그 액티비티의 클래스 이름을 가져옵니다. 반드시 모든 액티비티는 새로운 액티비티를 실행할 때 자신의 클래스 정보를 전달해야합니다.
        var lastActivityClass = Class.forName(classInfo!!.getString("className"))

        //화면 넘기기(고쳐야됩니다)
        //val intent = Intent(this, SubActivity::class.java)
        val intent2 = Intent(this, lastActivityClass)

        //CheckBox Listener 등록을 간소화시켰습니다
        var layout = findViewById<LinearLayout>(R.id.interestList)
        for (i in 0..layout.childCount) {
            var elem = layout!!.getChildAt(i)
            if (elem is CheckBox) {
                elem.setOnCheckedChangeListener(listener)
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
