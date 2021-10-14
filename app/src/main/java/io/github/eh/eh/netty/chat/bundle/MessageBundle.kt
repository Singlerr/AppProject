package io.github.eh.eh.netty.chat.bundle

import io.github.eh.eh.serverside.User

class MessageBundle(
    val image: ByteArray?,
    val message: String,
    val targetUser: User?,
    state: String?,
    var user: User,
    val me: Boolean,
    val time: String?
) {
    var targetUserId: String? = null

    constructor(
        message: String,
        targetUserId: String,
        state: String?,
        user: User,
        me: Boolean
    ) : this(null, message, null, null, user, me, null) {
        this.targetUserId = targetUserId
    }

    companion object {
        fun createMessage(
            image: ByteArray,
            text: String,
            targetUser: User,
            user: User,
            me: Boolean,
            time: String
        ): MessageBundle {
            return MessageBundle(image, text, targetUser, null, user, me, time)
        }

        fun createMessage(
            message: String,
            targetUserId: String,
            user: User,
            me: Boolean
        ): MessageBundle {
            return MessageBundle(message, targetUserId, null, user, me)
        }
    }
}