package io.github.eh.eh

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.asutils.MenuDialog
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.misc.FriendChatInfo
import io.github.eh.eh.netty.chat.ChatMessageHandler
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class FriendsActivity : AppCompatActivity() {
    private var friendView = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        var listView = findViewById<ListView>(R.id.frd_mtd_list)

        var user = Utils.getUser(intent)!!

        var matchedAdapter = FriendChatViewAdapter(
            applicationContext,
            R.layout.adapter_matched_or_friend,
            user
        )

        var friendAdapter = FriendChatViewAdapter(
            applicationContext,
            R.layout.adapter_matched_or_friend,
            user
        )

        CoroutineScope(Dispatchers.Main).launch {
            ChatMessageHandler.getInstance().getAllChatRooms().forEach { chatRoom ->
                //If a chatroom is friend-chatroom
                if (user.friends.contains(chatRoom.friendChatInfo.id)) {
                    friendAdapter.addFriendChatInfo(chatRoom.friendChatInfo)
                } else {
                    //Matched chatroom - temporary
                    matchedAdapter.addFriendChatInfo(chatRoom.friendChatInfo)

                }
            }
        }
        listView.adapter = friendAdapter
        show_friend.setOnClickListener {
            if (!friendView) {
                listView.adapter = friendAdapter
                show_friend.setBackgroundResource(R.drawable.friend_button_unfocused)
                show_matched.setBackgroundResource(R.drawable.matched_list_button_focused)
                friendView = false
            }
        }
        show_matched.setOnClickListener {
            if (friendView) {
                listView.adapter = matchedAdapter
                show_friend.setBackgroundResource(R.drawable.friend_button_focused)
                show_matched.setBackgroundResource(R.drawable.matched_list_button_unfocused)
                friendView = true
            }
        }

    }

    class FriendChatViewAdapter(val context: Context, val layout: Int, val user: User) :
        BaseAdapter() {

        private var friendChatInfo: Stack<FriendChatInfo> = Stack()

        override fun getCount(): Int {
            return friendChatInfo.size
        }

        override fun getItem(pos: Int): Any {
            return friendChatInfo[pos]
        }

        override fun getItemId(pos: Int): Long {
            return 0
        }

        fun addFriendChatInfo(frdChatInfo: FriendChatInfo) {
            friendChatInfo.add(frdChatInfo)
            notifyDataSetChanged()
        }

        fun addAll(col: Collection<FriendChatInfo>) {
            friendChatInfo.addAll(col)
            notifyDataSetChanged()
        }

        override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
            val current = friendChatInfo[pos]
            val view = LayoutInflater.from(context).inflate(layout, null)
            val frdImage = view.findViewById<ImageView>(R.id.frd_img)
            val frdName = view.findViewById<TextView>(R.id.frd_name)
            val frdInfo = view.findViewById<TextView>(R.id.frd_info)
            val frdTime = view.findViewById<TextView>(R.id.frd_time)

            var bitmap = BitmapFactory.decodeByteArray(current.img, 0, current.img.size)

            var gestureDetector =
                GestureDetector(context, object : GestureDetector.OnGestureListener {
                    override fun onDown(p0: MotionEvent?): Boolean {
                        return false
                    }

                    override fun onShowPress(p0: MotionEvent?) {
                    }

                    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                        return false
                    }

                    override fun onScroll(
                        p0: MotionEvent?,
                        p1: MotionEvent?,
                        p2: Float,
                        p3: Float
                    ): Boolean {
                        return false
                    }

                    override fun onLongPress(e: MotionEvent?) {
                        //TODO("길게 탭 했을 때 메뉴 띄우기")
                        var dialog = MenuDialog.Builder(context)
                            .menuItems(MenuDialog.MenuItem("차단") {
                                //블랙리스트에 저장
                                user.blackList.plus(current.id)
                                var bootstrap = HTTPBootstrap.builder()
                                    .token(user.password)
                                    .port(Env.HTTP_PORT)
                                    .build()
                            }).create()
                        dialog.show()
                    }

                    override fun onFling(
                        p0: MotionEvent?,
                        p1: MotionEvent?,
                        p2: Float,
                        p3: Float
                    ): Boolean {
                        return false
                    }

                })

            view.setOnTouchListener { _, motionEvent ->
                gestureDetector.onTouchEvent(motionEvent)
            }
            view.setOnClickListener {

            }
            frdImage.setImageBitmap(bitmap)
            frdName.text = current.name
            frdInfo.text = String.format("%s, %d세", current.sex, current.age)
            frdTime.text = current.time
            return view
        }

    }
}