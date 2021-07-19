package io.github.eh.eh.netty.chat.bundle

class MessageBundle private constructor(val message: String) {
    companion object {
        fun createMessage(text: String): MessageBundle {
            return MessageBundle(text)
        }
    }
}