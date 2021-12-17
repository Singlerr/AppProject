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
import io.github.eh.eh.db.ChatLogStorage
import io.github.eh.eh.netty.chat.ChatContext
import io.github.eh.eh.netty.chat.ChatMessageHandler
import io.github.eh.eh.netty.chat.ChatMessageListener
import io.github.eh.eh.netty.chat.ChatRoom
import io.github.eh.eh.netty.chat.bundle.MessageBundle
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var connectionHandler: ChatMessageHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        var chatStorage = ChatLogStorage(this, "chat", null, 1)

        var targetUserId = Utils.getTargetUserId(intent)!!
        var user = Utils.getUser(intent)!!

        var chatView = findViewById<ListView>(R.id.chat)

        var chatRoom = ChatMessageHandler.getInstance().getChatRoom(targetUserId!!)!!

        var adapter = ChatViewAdapter(applicationContext, chatRoom)

        CoroutineScope(Dispatchers.Main).launch {
            chatRoom.messages.forEach { msg ->
                adapter.addMessage(msg)
            }
        }
        chatView.adapter = adapter
        ChatMessageHandler.getInstance()
            .registerChatListener(chatRoom, object : ChatMessageListener {
                override fun onMessageRead(context: ChatContext, bundle: MessageBundle) {
                    chatRoom.addMessage(bundle)
                    chatStorage.insert(bundle)
                }
            })
        send.setOnClickListener {
            var msg = textMsg.text.toString()
            var bundle = MessageBundle.createMessage(
                msg,
                targetUserId,
                user.userId!!,
                Utils.getCurrentTime()
            )
            ChatMessageHandler.getInstance().writeMessage(bundle)
            adapter.addMessage(bundle)
            chatRoom.addMessage(bundle)
            chatStorage.insert(bundle)
        }
    }

    class ChatViewAdapter(val context: Context, val chatRoom: ChatRoom) : BaseAdapter() {

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


                var bitmap = BitmapFactory.decodeByteArray(
                    chatRoom.friendChatInfo.img,
                    0,
                    chatRoom.friendChatInfo.img.size
                )
                msgImage.setImageBitmap(bitmap)

                msgName.text = chatRoom.friendChatInfo.nickName

                msg.text = current.message

                msgTime.text = Utils.getTimeString(current.time)
                return view
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_you, null)
                val msgName = view.findViewById<TextView>(R.id.oppName)
                val msg = view.findViewById<TextView>(R.id.chatMsg)
                val msgTime = view.findViewById<TextView>(R.id.time)

                msgName.text = chatRoom.friendChatInfo.nickName

                msg.text = current.message
                msgTime.text = Utils.getTimeString(current.time)
                return view
            }
        }

    }
}