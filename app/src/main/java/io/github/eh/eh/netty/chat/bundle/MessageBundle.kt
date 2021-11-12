package io.github.eh.eh.netty.chat.bundle

import com.fasterxml.jackson.annotation.JsonTypeInfo
import lombok.Getter

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Getter
class MessageBundle(
    var message: String,
    var targetUserId: String,
    var ownerId: String,
    val time: String
) {
    lateinit var state: String
    var me: Boolean = false

    companion object {
        fun createMessage(
            message: String,
            targetUserId: String,
            ownerId: String,
            time: String
        ): MessageBundle {
            return MessageBundle(message, targetUserId, ownerId, time)
        }
    }
}