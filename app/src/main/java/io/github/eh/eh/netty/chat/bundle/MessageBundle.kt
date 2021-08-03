package io.github.eh.eh.netty.chat.bundle

import io.github.eh.eh.serverside.User

class MessageBundle(val message: String, val targetUserId: String, state: String, var user: User) {
    companion object {
        fun createMessage(text: String, targetUserId: String, user: User): MessageBundle {
            return MessageBundle(text, targetUserId, "", user)
        }
    }
}