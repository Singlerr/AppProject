package io.github.eh.eh.netty.chat.bundle

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import lombok.Getter

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Getter
class MessageBundle(
    var message: String,
    var targetUserId: String,
    var ownerId: String,
    val time: String,
    @JsonIgnore var me: Boolean = false,
    var state: String = "SEND"
) {

    constructor() : this("", "", "", "")

    companion object {
        fun createMessage(
            message: String,
            targetUserId: String,
            ownerId: String,
            time: String,
            me: Boolean = false,
            state: String = "SEND"
        ): MessageBundle {
            return MessageBundle(message, targetUserId, ownerId, time, me, state)
        }
    }
}