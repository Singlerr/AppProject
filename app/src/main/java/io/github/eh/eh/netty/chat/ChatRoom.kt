package io.github.eh.eh.netty.chat

import io.github.eh.eh.misc.FriendChatInfo
import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.User

class ChatRoom(val friendChatInfo: FriendChatInfo, var messages: List<MessageBundle>) {
    fun addMessage(message: MessageBundle) {
        messages.plus(message)
    }

    companion object {
        fun createChatRoom(ownerUser: User, targetUser: User): ChatRoom {
            val friendChatInfo = FriendChatInfo(
                img = targetUser.image!!,
                name = targetUser.name!!,
                nickName = targetUser.nickName!!,
                id = targetUser.userId!!,
                sex = targetUser.sex!!,
                age = targetUser.age!!,
                time = null,
                ownerId = ownerUser.userId!!
            )
            return ChatRoom(friendChatInfo = friendChatInfo, messages = ArrayList())
        }
    }
}