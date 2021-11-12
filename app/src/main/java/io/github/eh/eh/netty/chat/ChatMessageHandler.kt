package io.github.eh.eh.netty.chat

import io.github.eh.eh.Env
import io.github.eh.eh.netty.chat.bundle.MessageBundle

class ChatMessageHandler {

    private var chats: HashMap<String, ChatRoom> = HashMap()

    private var chatListeners: HashMap<String, ChatMessageListener> = HashMap()


    private lateinit var context: ChatContext

    private var bootstrap: ChatClientBootstrap =
        ChatClientBootstrap.getInstance(Env.CHAT_POOL_URL, Env.CHAT_PORT)


    private constructor()


    fun registerChatListener(chatRoom: ChatRoom, listener: ChatMessageListener) {
        chatListeners[chatRoom.friendChatInfo.ownerId] = listener
    }

    fun deregister(chatRoom: ChatRoom) {
        chatListeners.remove(chatRoom.friendChatInfo.ownerId)
    }

    fun containsChatRoom(userId: String): Boolean {
        return chats.containsKey(userId)
    }

    fun putChatRoom(userId: String, chatRoom: ChatRoom) {
        chats[userId] = chatRoom
    }

    fun putChatRoom(userId: String, chatRoom: ChatRoom, listener: ChatMessageListener) {
        chats[userId] = chatRoom
        chatListeners[userId] = listener
    }

    fun getChatRoom(userId: String): ChatRoom? {
        return chats[userId]
    }

    fun getAllChatRooms(): List<ChatRoom> {
        return chats.values.toList()
    }

    fun openChatMessageListener() {
        context = bootstrap.startConnection(object : MessageHandler {
            override fun onMessageReceived(context: ChatContext, bundle: MessageBundle?) {
                if (chatListeners.containsKey(bundle!!.ownerId) && containsChatRoom(bundle.ownerId)) {
                    context.chatRoom = getChatRoom(bundle.ownerId)
                    chatListeners[bundle.ownerId]!!.onMessageRead(context, bundle)
                }
            }
        })
    }

    fun writeMessage(messageBundle: MessageBundle) {
        context.writeAndFlush(messageBundle)
    }


    companion object {
        private var instance: ChatMessageHandler = ChatMessageHandler()
        fun getInstance(): ChatMessageHandler {
            return instance
        }
    }
}