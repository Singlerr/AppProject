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
import org.xmlpull.v1.XmlPullParser


class InterestListActivity : AppCompatActivity() {

    private var user: User? = null

    //체크했을 때 체크박스를 문자열에 저장하기 위한 함수 생성
    private fun Food(array1: String) {
        var json = user!!.parseInterest!!.getJSONArray("food")
        if (!Utils.hasValue(json, array1))
            json.put(array1)
    }

    private fun Hobby(array2: String) {
        var json = user!!.parseInterest!!.getJSONArray("hobby")
        if (!Utils.hasValue(json, array2))
            json.put(array2)
    }

    private fun Place(array3: String) {
        var json = user!!.parseInterest!!.getJSONArray("place")
        if (!Utils.hasValue(json, array3))
            json.put(array3)
    }

    //체크박스 눌렀을 때 위의 함수들 실행하기
    //다른 액티비티에서 작업하다가 붙여넣기 해서 오류가 발생했습니다.


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val listener by lazy {
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            when (buttonView.id) {
                R.id.checkHansik -> Food("한식")
                R.id.checkYangsik -> Food("양식")
                R.id.checkJunsik -> Food("중식")
                R.id.checkIlsik -> Food("일식")
                R.id.checkAsian -> Food("아시아음식")
                R.id.checkDessert -> Food("디저트")
                R.id.checkDrink -> Food("술")
                R.id.checkVeg -> Food("비건")
                R.id.checkWorkout -> Hobby("헬스")
                R.id.checkReading -> Hobby("독서")
                R.id.checkMusic -> Hobby("음악")
                R.id.checkMovie -> Hobby("영화")
                R.id.checkSwimming -> Hobby("수영")
                R.id.checkBicycle -> Hobby("자전거")
                R.id.checkTennis -> Hobby("테니스")
                R.id.checkSoccer -> Hobby("축구")
                R.id.checkMedi -> Hobby("명상")
                R.id.checkStudy -> Hobby("공부")
                R.id.checkPicture -> Hobby("사진")
                R.id.checkGolf -> Hobby("골프")
                R.id.checkKorea -> Place("한국")
                R.id.checkBri -> Place("영국")
                R.id.checkFra -> Place("프랑스")
                R.id.checkChi -> Place("중국")
                R.id.checkJap -> Place("일본")
                R.id.checkAmer -> Place("미국")
                R.id.checkAust -> Place("호주")
                R.id.checkHonk -> Place("홍콩")
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
        for(i in 0..layout.childCount){
            var elem = layout!!.getChildAt(i)
            if(elem is CheckBox){
                elem.setOnCheckedChangeListener(listener)
            }
        }
       // binding.complete.setOnClickListener { startActivity(intent) }
       // binding.backspace.setOnClickListener { LastActivity(intent2) }

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
