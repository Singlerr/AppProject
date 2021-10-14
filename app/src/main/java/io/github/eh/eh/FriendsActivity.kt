package io.github.eh.eh

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.misc.FriendChatInfo
import io.github.eh.eh.misc.FriendChatInfoList
import kotlinx.android.synthetic.main.activity_friends.*
import java.util.*

class FriendsActivity : AppCompatActivity() {
    private var friendAdapter: FriendChatViewAdapter? = null
    private var matchedAdapter: FriendChatViewAdapter? = null
    private var friendView = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        var listView = findViewById<ListView>(R.id.frd_mtd_list)

        var frdBootstrap = HTTPBootstrap.builder()
            .port(Env.HTTP_PORT)
            .host(Env.REQ_FRIEND_LIST_URL)
            .streamHandler(object : StreamHandler {
                override fun onWrite(outputStream: HTTPContext) {
                    TODO("Not yet implemented")
                }

                override fun onRead(obj: Any?) {
                    if (obj is FriendChatInfoList) {
                        friendAdapter = FriendChatViewAdapter(
                            applicationContext,
                            R.layout.adapter_matched_or_friend
                        )
                        friendAdapter!!.addAll(obj.list)
                        listView.adapter = friendAdapter!!
                    }
                }
            }).build()
        var mtdBootstrap = HTTPBootstrap.builder()
            .port(Env.HTTP_PORT)
            .host(Env.REQ_MATCHED_LIST_URL)
            .streamHandler(object : StreamHandler {
                override fun onWrite(outputStream: HTTPContext) {
                    TODO("Not yet implemented")
                }

                override fun onRead(obj: Any?) {
                    if (obj is FriendChatInfoList) {
                        matchedAdapter = FriendChatViewAdapter(
                            applicationContext,
                            R.layout.adapter_matched_or_friend
                        )
                        matchedAdapter!!.addAll(obj.list)
                    }
                }

            }).build()
        frdBootstrap.submit()
        mtdBootstrap.submit()

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

    class FriendChatViewAdapter(val context: Context, val layout: Int) : BaseAdapter() {

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

            frdImage.setImageBitmap(bitmap)
            frdName.text = current.name
            frdInfo.text = String.format("%s, %d세", current.sex, current.age)
            frdTime.text = current.time
            return view
        }

    }
}