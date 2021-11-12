package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle


interface ChatMessageListener {
    fun onMessageRead(context: ChatContext, bundle: MessageBundle)
}