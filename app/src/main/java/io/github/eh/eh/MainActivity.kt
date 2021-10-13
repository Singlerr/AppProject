package io.github.eh.eh

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.Env.Bundle.BUNDLE_NAME
import io.github.eh.eh.Env.Bundle.USER_BUNDLE
import io.github.eh.eh.asutils.MatchedDialog
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.misc.Restaurant
import io.github.eh.eh.misc.RestaurantList
import io.github.eh.eh.netty.MatchingCallback
import io.github.eh.eh.netty.MatchingClientBootstrap
import io.github.eh.eh.netty.UserWrapper
import io.github.eh.eh.serverside.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val currentAnimations: HashMap<Int, Pair<TranslateAnimation, ValueAnimator>> = HashMap()
    private lateinit var user: User
    private lateinit var wrapper: UserWrapper
    private val barArray = arrayOf(
        R.id.bar_1,
        R.id.bar_2,
        R.id.bar_3,
        R.id.bar_4,
        R.id.bar_5,
        R.id.bar_6,
        R.id.bar_7,
        R.id.bar_8,
        R.id.bar_9,
        R.id.bar_10,
        R.id.bar_11
    )

    private fun setSexScope(desiredTarget: DesiredTarget, sexScope: SexScope) {
        desiredTarget.desiredSexScope = sexScope.sexScope
    }

    private fun setAgeScope(desiredTarget: DesiredTarget, ageScope: AgeScope) {
        desiredTarget.desiredAgeScope = ageScope.scope
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.hasExtra(BUNDLE_NAME)) {
            var bundle = intent.getBundleExtra(BUNDLE_NAME)!!
            user = bundle.getSerializable(USER_BUNDLE) as User

            var desiredTarget = DesiredTarget()
            desiredTarget.user = user
            desiredTarget.gpsData = GPSData()
            wrapper = UserWrapper.getInstance(user, object : MatchingCallback {
                override fun onMatched(userWrapper: UserWrapper?, targetUser: User?) {
                    stopAnimation()
                    var dialog = MatchedDialog.Builder(this@MainActivity)
                        .name(targetUser!!.nickName)
                        .info(targetUser)
                        .positiveButton("수락") {
                            //수락했을 때
                        }
                        .create()
                    dialog.show()
                }

            }, desiredTarget)
        }
        btn_startMatching.setOnClickListener {
            startAnimation()
            var bootstrap = MatchingClientBootstrap.getInstance(Env.MATCHING_POOL_URL, Env.PORT)
            CoroutineScope(Dispatchers.IO).launch {
                bootstrap!!.submit(wrapper)
            }
        }
        btn_sex_all.setOnClickListener {
            btn_sex_all.setBackgroundResource(R.drawable.button_rounded_high)
            btn_sex_female.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_sex_male.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            setSexScope(wrapper.desiredTarget, SexScope.SCOPE_ALL)
        }
        btn_sex_male.setOnClickListener {
            btn_sex_all.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_sex_female.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_sex_male.setBackgroundResource(R.drawable.button_rounded_high)
            setSexScope(wrapper.desiredTarget, SexScope.SCOPE_MALE)
        }
        btn_sex_female.setOnClickListener {
            btn_sex_all.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_sex_female.setBackgroundResource(R.drawable.button_rounded_high)
            btn_sex_male.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            setSexScope(wrapper.desiredTarget, SexScope.SCOPE_FEMALE)
        }

        btn_age_random.setOnClickListener {
            btn_age_random.setBackgroundResource(R.drawable.button_rounded_high)
            btn_age_1.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_2.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_3.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            setAgeScope(wrapper.desiredTarget, AgeScope.SCOPE_RANDOM)
        }
        btn_age_1.setOnClickListener {
            btn_age_random.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_1.setBackgroundResource(R.drawable.button_rounded_high)
            btn_age_2.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_3.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            setAgeScope(wrapper.desiredTarget, AgeScope.SCOPE_20_25)
        }
        btn_age_2.setOnClickListener {
            btn_age_random.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_1.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_2.setBackgroundResource(R.drawable.button_rounded_high)
            btn_age_3.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            setAgeScope(wrapper.desiredTarget, AgeScope.SCOPE_26_30)
        }
        btn_age_3.setOnClickListener {
            btn_age_random.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_1.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_2.setBackgroundResource(R.drawable.button_rounded_high_unclicked)
            btn_age_3.setBackgroundResource(R.drawable.button_rounded_high)
            setAgeScope(wrapper.desiredTarget, AgeScope.SCOPE_31)
        }
        initializeRestaurantList()
    }

    private fun startAnimation() {
        for (id in barArray) {
            var random = Random()
            var tAnim: TranslateAnimation = if (Math.random() > 0.5) {
                TranslateAnimation(50.0f, -50.0f, 0.0f, 0.0f)
                //TranslateAnimation(-((random.nextInt(4) + 6)*10).toFloat(),((random.nextInt(4) + 6)*10).toFloat(),0.0f,0.0f)
            } else {
                TranslateAnimation(-50.0f, 50.0f, 0.0f, 0.0f)
                //TranslateAnimation(((random.nextInt(4) + 6)*10).toFloat(),-((random.nextInt(4) + 6)*10).toFloat(),0.0f,0.0f)
            }
            tAnim.duration = ((random.nextInt(2) + 1) * 1000).toLong()
            tAnim.repeatCount = TranslateAnimation.INFINITE
            tAnim.repeatMode = TranslateAnimation.REVERSE
            tAnim.startOffset = ((random.nextInt(1) + Math.random()) * 1000).toLong()
            var view = findViewById<View>(id)
            view.startAnimation(tAnim)
            val colorAnimation =
                ValueAnimator.ofArgb(Color.parseColor("#f8b547"), Color.parseColor("#fde8c6"))
                    .apply {
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        duration = 2000
                        startDelay = (random.nextInt(2) * 1000).toLong()
                        addUpdateListener {
                            view.background.setTint(it.animatedValue as Int)
                        }
                    }
            colorAnimation.start()
            currentAnimations[id] = Pair<TranslateAnimation, ValueAnimator>(tAnim, colorAnimation)
        }
    }

    private fun initializeRestaurantList() {
        val bootstrap = HTTPBootstrap.builder()
            .host(Env.REQ_RESTAURANT_LIST_URL)
            .port(Env.HTTP_PORT)
            .streamHandler(object : StreamHandler {
                override fun onWrite(outputStream: HTTPContext) {
                    //Nothing to write
                }

                override fun onRead(obj: Any?) {
                    if (obj is RestaurantList) {
                        val rstView = findViewById<ListView>(R.id.rstView)
                        var restaurantViewAdapter = RestaurantViewAdapter(applicationContext)
                        restaurantViewAdapter.addAll(obj.restaurants)
                        rstView.adapter = restaurantViewAdapter
                    }
                }

            })
            .build()
        bootstrap.submit()
    }

    private fun stopAnimation() {
        currentAnimations.forEach {
            it.value.first.cancel()
            it.value.second.cancel()
        }
    }

    class RestaurantViewAdapter(val context: Context) : BaseAdapter() {

        private var restaurants: Stack<Restaurant> = Stack()

        override fun getCount(): Int {
            return restaurants.size
        }

        override fun getItem(pos: Int): Any {
            return restaurants[pos]
        }

        override fun getItemId(pos: Int): Long {
            return 0
        }

        fun addRestaurant(restaurant: Restaurant) {
            restaurants.add(restaurant)
        }

        fun addAll(col: Collection<Restaurant>) {
            restaurants.addAll(col)
        }

        override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
            val current = restaurants[pos]
            val view = LayoutInflater.from(context).inflate(R.layout.adapter_restaurant, null)
            val rstImage = view.findViewById<ImageView>(R.id.restImage)
            val rstName = view.findViewById<TextView>(R.id.restName)
            val rstAddress = view.findViewById<TextView>(R.id.restAddress)
            val rstRate = view.findViewById<TextView>(R.id.restRate)


            var bitmap = BitmapFactory.decodeByteArray(current.image, 0, current.image.size)
            rstImage.setImageBitmap(bitmap)

            rstName.text = current.name

            rstAddress.text = current.address

            rstRate.text = current.rate.toString()

            return view
        }

    }
}