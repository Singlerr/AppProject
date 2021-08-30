package io.github.eh.eh


import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_interest_list.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InterestListActivity : AppCompatActivity(), View.OnClickListener {
    private var user: User? = null
    var foodArraySet = mutableSetOf<String>()
    var hobbyArraySet = mutableSetOf<String>()
    var placeArraySet = mutableSetOf<String>()
    private var stateFood = 0
    private var stateHobby = 0
    private var statePlace = 0

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.checkHansik -> setFood(checkHansik)
            R.id.checkYangsik -> setFood(checkYangsik)
            R.id.checkJunsik -> setFood(checkJunsik)
            R.id.checkIlsik -> setFood(checkIlsik)
            R.id.checkAsian -> setFood(checkAsian)
            R.id.checkDessert -> setFood(checkDessert)
            R.id.checkDrink -> setFood(checkDrink)
            R.id.checkVeg -> setFood(checkVeg)
            R.id.checkWorkout -> setHobby(checkWorkout)
            R.id.checkReading -> setHobby(checkReading)
            R.id.checkMusic -> setHobby(checkMusic)
            R.id.checkMovie -> setHobby(checkMovie)
            R.id.checkSwimming -> setHobby(checkSwimming)
            R.id.checkBicycle -> setHobby(checkBicycle)
            R.id.checkTennis -> setHobby(checkTennis)
            R.id.checkSoccer -> setHobby(checkSoccer)
            R.id.checkMedi -> setHobby(checkMedi)
            R.id.checkStudy -> setHobby(checkStudy)
            R.id.checkPicture -> setHobby(checkPicture)
            R.id.checkGolf -> setHobby(checkGolf)
            R.id.checkKorea -> setPlace(checkKorea)
            R.id.checkBri -> setPlace(checkBri)
            R.id.checkFra -> setPlace(checkFra)
            R.id.checkChi -> setPlace(checkChi)
            R.id.checkJap -> setPlace(checkJap)
            R.id.checkAmer -> setPlace(checkAmer)
            R.id.checkAust -> setPlace(checkAust)
            R.id.checkHonk -> setPlace(checkHonk)
        }
    }
    //체크했을 때 체크박스를 Set에 저장하기 위한 함수 생성
    fun setFood(foodButton: Button) {
        var foodString = foodButton.text.toString()

        if (foodArraySet.contains(foodString) == false) {
            foodArraySet.add(foodString)
            foodButton.setBackgroundResource(R.drawable.button_rounded_medium)
            stateFood = 1
            checkConditions()
        }
        else if(foodArraySet.contains(foodString) == true) {
            foodArraySet.remove(foodString)
            foodButton.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
            if(foodArraySet.isEmpty() == true) {
                stateFood = 0
                checkConditions()
            }
        }


    }

    fun setHobby(hobbyButton: Button) {
        var hobbyString = hobbyButton.text.toString()

        if (hobbyArraySet.contains(hobbyString) == false) {
            hobbyArraySet.add(hobbyString)
            hobbyButton.setBackgroundResource(R.drawable.button_rounded_medium)
            stateHobby = 1
            checkConditions()
        }
        else if(hobbyArraySet.contains(hobbyString) == true) {
            hobbyArraySet.remove(hobbyString)
            hobbyButton.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
            if(hobbyArraySet.isEmpty() == true) {
                stateHobby = 0
                checkConditions()
            }
        }

    }

    fun setPlace(placeButton: Button) {
        var placeString = placeButton.text.toString()

        if (placeArraySet.contains(placeString) == false) {
            placeArraySet.add(placeString)
            placeButton.setBackgroundResource(R.drawable.button_rounded_medium)
            statePlace = 1
            checkConditions()
        }
        else if(placeArraySet.contains(placeString) == true) {
            placeArraySet.remove(placeString)
            placeButton.setBackgroundResource(R.drawable.button_rounded_medium_interest_disabled)
            if(placeArraySet.isEmpty() == true) {
                statePlace = 0
                checkConditions()
            }
        }

    }

    private fun checkConditions(): Boolean {
        var stateSum = stateFood + stateHobby + statePlace
        return if (stateSum == 3) {
            complete.setBackgroundResource(R.drawable.button_rounded_medium)
            true
        } else {
            complete.setBackgroundResource(R.drawable.button_rounded_medium_disabled)
            false
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Slide(Gravity.RIGHT)
        }
        setContentView(R.layout.activity_interest_list)
        user = Utils.getUser(intent)
        //이전 액티비티에서 그 액티비티의 클래스 이름을 가져옵니다. 반드시 모든 액티비티는 새로운 액티비티를 실행할 때 자신의 클래스 정보를 전달해야합니다.
        var profileSettingActivity = Class.forName(Utils.getClassName(intent))

        //화면 넘기기
        //val intent = Intent(this, SubActivity::class.java)
        val intent2 = Intent(this, ProfileSettingActivity::class.java)

        // 관심사 Set과 프로필세팅 액티비티의 데이터를 서버로 전송
        complete.setOnClickListener {
            if (checkConditions()) {
                if (foodArraySet == null && hobbyArraySet == null && placeArraySet == null) {
                    return@setOnClickListener
                }
                val bootstrap: HTTPBootstrap = HTTPBootstrap.builder()
                    .port(1300)
                    .host(Env.API_URL)
                    .streamHandler(object : StreamHandler {
                        override fun onWrite(outputStream: HTTPContext) {
                            val user = User()
                            var userData = intent2.getBundleExtra("user")
                            user.foodInterests = foodArraySet
                            user.hobbyInterests = hobbyArraySet
                            user.placeInterests = placeArraySet
                            outputStream.write(user)
                        }

                        override fun onRead(obj: Any?) {
                            ToMainIntent(obj as User?)
                        }
                    }).build()
                bootstrap.submit()
            }
        }

        checkHansik.setOnClickListener(this)
        checkYangsik.setOnClickListener(this)
        checkJunsik.setOnClickListener(this)
        checkIlsik.setOnClickListener(this)
        checkAsian.setOnClickListener(this)
        checkDessert.setOnClickListener(this)
        checkDrink.setOnClickListener(this)
        checkVeg.setOnClickListener(this)
        checkWorkout.setOnClickListener(this)
        checkReading.setOnClickListener(this)
        checkMusic.setOnClickListener(this)
        checkMovie.setOnClickListener(this)
        checkSwimming.setOnClickListener(this)
        checkBicycle.setOnClickListener(this)
        checkTennis.setOnClickListener(this)
        checkSoccer.setOnClickListener(this)
        checkMedi.setOnClickListener(this)
        checkStudy.setOnClickListener(this)
        checkPicture.setOnClickListener(this)
        checkGolf.setOnClickListener(this)
        checkKorea.setOnClickListener(this)
        checkBri.setOnClickListener(this)
        checkFra.setOnClickListener(this)
        checkChi.setOnClickListener(this)
        checkJap.setOnClickListener(this)
        checkAmer.setOnClickListener(this)
        checkAust.setOnClickListener(this)
        checkHonk.setOnClickListener(this)

        // binding.complete.setOnClickListener { startActivity(intent) }
        backspace.setOnClickListener { startActivity(intent2) }

    }

    private fun ToMainIntent(user: User?) {
        val toMainIntent = Intent(this, MainActivity::class.java)
        var bundle: Bundle = Bundle()
        bundle.putSerializable("user", user)
        toMainIntent.putExtra("user", bundle)
        startActivity(toMainIntent)
    }

}
