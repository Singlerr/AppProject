package io.github.eh.eh.netty.chat

import io.github.eh.eh.misc.FriendChatInfo
import io.github.eh.eh.netty.chat.bundle.MessageBundle

class ChatRoom(val friendChatInfo: FriendChatInfo, var messages: List<MessageBundle>) {
    fun addMessage(message: MessageBundle) {
        messages.plus(message)
    }
}