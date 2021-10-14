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
import io.github.eh.eh.asutils.Utils
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.misc.Restaurant
import io.github.eh.eh.netty.chat.ChatClientBootstrap
import io.github.eh.eh.netty.chat.ChatContext
import io.github.eh.eh.netty.chat.MessageHandler
import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.User
import kotlinx.android.synthetic.main.activity_chat_room.*
import java.util.*

class ChatRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        var bundle = intent.getBundleExtra(Env.Bundle.BUNDLE_NAME)!!
        var targetUserId = bundle.getString(Env.Bundle.TARGET_USER_ID_BUNDLE)!!
        var user = bundle.getSerializable(Env.Bundle.USER_BUNDLE)!! as User

        var chatView = findViewById<ListView>(R.id.chat)
        var adapter = ChatViewAdapter(applicationContext)
        chatView.adapter = adapter
        var chatClientBootstrap = ChatClientBootstrap.getInstance(Env.CHAT_POOL_URL, Env.CHAT_PORT)
        chatClientBootstrap.startConnection(object : MessageHandler {
            override fun onMessageReceived(context: ChatContext, bundle: MessageBundle?) {
                if (!bundle!!.me) {
                    adapter.addMessage(bundle!!)
                }
            }
        })
        send.setOnClickListener {
            var msg = textMsg.text.toString()
            chatClientBootstrap.chatContext!!.sendMessage(msg,targetUserId,user)
            adapter.addMessage(MessageBundle.createMessage(msg,targetUserId,user,true))
        }
    }

    class ChatViewAdapter(val context: Context) : BaseAdapter() {

        private var messages: Stack<MessageBundle> = Stack()

        override fun getCount(): Int {
            return messages.size
        }

        override fun getItem(pos: Int): Any {
            return messages[pos]
        }

        override fun getItemId(pos: Int): Long {
            return 0
        }

        fun addMessage(message: MessageBundle) {
            messages.add(message)
            notifyDataSetChanged()
        }

        fun addAll(col: Collection<MessageBundle>) {
            messages.addAll(col)
            notifyDataSetChanged()
        }

        override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
            val current = messages[pos]
            if (current.me) {
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_me, null)
                val msgImage = view.findViewById<ImageView>(R.id.prfImage)
                val msgName = view.findViewById<TextView>(R.id.oppName)
                val msg = view.findViewById<TextView>(R.id.chatMsg)
                val msgTime = view.findViewById<TextView>(R.id.time)


                var bitmap = BitmapFactory.decodeByteArray(current.image, 0, current.image!!.size)
                msgImage.setImageBitmap(bitmap)

                msgName.text = current.targetUser!!.nickName

                msg.text = current.message

                msgTime.text = Utils.getTimeString(current.time!!)
                return view
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_you, null)
                val msgName = view.findViewById<TextView>(R.id.oppName)
                val msg = view.findViewById<TextView>(R.id.chatMsg)
                val msgTime = view.findViewById<TextView>(R.id.time)

                msgName.text = current.targetUser!!.nickName

                msg.text = current.message
                msgTime.text = Utils.getTimeString(current.time!!)
                return view
            }
        }

    }
}