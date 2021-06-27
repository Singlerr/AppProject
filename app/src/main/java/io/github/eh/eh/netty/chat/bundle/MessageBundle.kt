package io.github.eh.eh.netty.chat.bundle

import lombok.Getter

@Getter
class MessageBundle private constructor(private val message: String) {
    companion object {
        fun createMessage(text: String): MessageBundle {
            return MessageBundle(text)
        }
    }
}