package io.github.eh.eh

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import io.github.eh.eh.asutils.IAlertDialog
import io.github.eh.eh.asutils.MatchedDialog
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.db.ChatLogStorage
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.RequestBundle
import io.github.eh.eh.misc.FriendChatInfo
import io.github.eh.eh.misc.FriendChatInfoList
import io.github.eh.eh.misc.Restaurant
import io.github.eh.eh.misc.RestaurantList
import io.github.eh.eh.netty.MatchingCallback
import io.github.eh.eh.netty.MatchingClientBootstrap
import io.github.eh.eh.netty.UserWrapper
import io.github.eh.eh.netty.chat.*
import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_restaurant_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val currentAnimations: HashMap<Int, Pair<TranslateAnimation, ValueAnimator>> = HashMap()
    private lateinit var user: User
    private lateinit var wrapper: UserWrapper


    @SuppressLint("MissingPermission")
    private val request = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            var bootstrap = MatchingClientBootstrap.getInstance(Env.MATCHING_POOL_URL, Env.PORT)
            var locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var locListener = LocationListener {
                var gpsData = GPSData(it.latitude, it.longitude)
                wrapper.desiredTarget.gpsData = gpsData
                CoroutineScope(Dispatchers.IO).launch {
                    bootstrap!!.submit(wrapper)
                }
            }
            locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,
                10.0f,
                locListener
            )
        } else {
            var dialog = IAlertDialog.Builder(this@MainActivity)
                .message("위치 정보 사용에 동의해주셔야 매칭을 시작할 수 있습니다.")
                .positiveButton("확인") { d, _ ->
                    d.dismiss()
                }
                .title("위치 정보 동의")
                .create()
            dialog.show()
        }
    }

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
        user = Utils.getUser(intent)!!
        var storage = ChatLogStorage(this@MainActivity, "chat", null, 1)
        var bitmap = BitmapFactory.decodeByteArray(user.image, 0, user.image!!.size)
        profileImg.setImageBitmap(bitmap)
        var clone = user.clone()
        clone.image = null
        var desiredTarget = DesiredTarget()
        desiredTarget.user = clone
        wrapper = UserWrapper.getInstance(clone, object : MatchingCallback {
            override fun onMatched(userWrapper: UserWrapper?, targetUser: User?) {
                CoroutineScope(Dispatchers.Main).launch {
                    stopAnimation()
                    var json = targetUser!!.parseInterest!!
                    var food = json.getJSONArray("food").getString(0)
                    var hobby = json.getJSONArray("hobby").getString(0)
                    var place = json.getJSONArray("place").getString(0)
                    var dialog = MatchedDialog.Builder(this@MainActivity)
                        .name(targetUser.nickName)
                        .info(targetUser)
                        .positiveButton("수락") { dialog, _ ->
                            //수락했을 때
                            var chatRoom = ChatRoom.createChatRoom(user,targetUser!!)
                            ChatMessageHandler.getInstance().putChatRoom(targetUser.userId!!,chatRoom)
                            var intent = Intent(this@MainActivity, FriendsActivity::class.java)
                            Utils.setEssentialData(intent=intent,user=user, className = this::class.java.name)
                            startActivity(intent)
                            dialog.dismiss()
                        }.negativeButton("거절") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .int(1, food)
                        .int(2, hobby)
                        .int(3, place)
                        .image(targetUser.image!!)
                        .create()
                    dialog.show()
                }
            }

        }, desiredTarget)
        btn_startMatching.setOnClickListener {
            startAnimation()
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                var bootstrap = MatchingClientBootstrap.getInstance(Env.MATCHING_POOL_URL, Env.PORT)
                var locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var locListener = LocationListener {
                    var gpsData = GPSData(it.latitude, it.longitude)
                    wrapper.desiredTarget.gpsData = gpsData
                    CoroutineScope(Dispatchers.IO).launch {
                        bootstrap!!.submit(wrapper)
                    }
                }
                locManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000L,
                    10.0f,
                    locListener
                )
            } else {
                request.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                request.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
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
        CoroutineScope(Dispatchers.IO).launch {
            initializeRestaurantList()
        }
        var frdBootstrap = HTTPBootstrap.builder()
            .port(Env.HTTP_PORT)
            .host(Env.REQ_FRIEND_LIST_URL)
            .method(HTTPBootstrap.GET_METHOD)
            .token(user.password)
            .streamHandler(object : StreamHandler {
                override fun onWrite(outputStream: HTTPContext) {
                    var bundle = RequestBundle()
                    bundle.setMessage(user)
                    outputStream.write(bundle)
                }

                override fun onRead(obj: Any?) {
                    if (obj is FriendChatInfoList) {
                        var list = obj.list
                        list.forEach {
                            var chatRoom = ChatRoom(it, ArrayList())
                            ChatMessageHandler.getInstance()
                                .putChatRoom(
                                    chatRoom.friendChatInfo.ownerId,
                                    chatRoom,
                                    object : ChatMessageListener {
                                        override fun onMessageRead(
                                            context: ChatContext,
                                            bundle: MessageBundle
                                        ) {

                                            //There are unloaded messages yet.
                                            if (storage.select(chatRoom.friendChatInfo.ownerId).count > chatRoom.messages.size) {
                                                //Load messages from db
                                                var cursor =
                                                    storage.select(chatRoom.friendChatInfo.ownerId)
                                                if (cursor.moveToFirst()) {
                                                    while (cursor.isAfterLast) {
                                                        var messageBundle =
                                                            MessageBundle.createMessage(
                                                                message = cursor.getString(1),
                                                                targetUserId = chatRoom.friendChatInfo.id,
                                                                ownerId = chatRoom.friendChatInfo.ownerId,
                                                                cursor.getString(2)
                                                            )
                                                        messageBundle.me =
                                                            cursor.getInt(3) as Boolean
                                                        chatRoom.messages.plus(messageBundle)
                                                    }
                                                }
                                            }
                                        }

                                    })
                        }
                    }
                }
            }).build()

        CoroutineScope(Dispatchers.IO).launch {
            frdBootstrap.submit()
            loadChatRooms(user)
        }
        CoroutineScope(Dispatchers.IO).launch {
            ChatMessageHandler.getInstance().openChatMessageListener()
        }
    }


    private fun loadChatRooms(user: User) {
        var chatLogStorage = ChatLogStorage(this, "chat", null, 1)
        var unregisteredIds = arrayOf<String>()
        chatLogStorage.tables().forEach { userId ->
            if (!ChatMessageHandler.getInstance().containsChatRoom(userId)) {
                unregisteredIds.plus(String.format("%s,%s", user.userId, userId))
            }
        }
        var reqInfoBootstrap = HTTPBootstrap.builder()
            .host(Env.REQ_FRIEND_INFO)
            .port(Env.HTTP_PORT)
            .method(HTTPBootstrap.GET_METHOD)
            .token(user.password)
            .streamHandler(object : StreamHandler {
                override fun onWrite(outputStream: HTTPContext) {
                    var bundle = RequestBundle()
                    bundle.setMessage(unregisteredIds)
                    outputStream.write(bundle)
                }

                override fun onRead(obj: Any?) {
                    if (obj is HashMap<*, *>) {
                        var map = obj as HashMap<String, FriendChatInfo>
                        map.forEach { (_, info) ->
                            var chatRoom = ChatRoom(info, ArrayList())
                            ChatMessageHandler.getInstance().putChatRoom(info.ownerId, chatRoom)
                        }
                    }
                }

            })
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            reqInfoBootstrap.submit()
        }
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

    private fun startDefaultAnimation() {
        barArray.forEach {
            var view = findViewById<View>(it)
            var random = Random()
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
        }
    }

    private fun initializeRestaurantList() {
        val bootstrap = HTTPBootstrap.builder()
            .host(Env.REQ_RESTAURANT_LIST_URL)
            .port(Env.HTTP_PORT)
            .method(HTTPBootstrap.GET_METHOD)
            .token(user.password)
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
            findViewById<View>(it.key).background.setTint(Color.parseColor("#f8b547"))
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
            notifyDataSetChanged()
        }

        fun addAll(col: Collection<Restaurant>) {
            restaurants.addAll(col)
            notifyDataSetChanged()
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