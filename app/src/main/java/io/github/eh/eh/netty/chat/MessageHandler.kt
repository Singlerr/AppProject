package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle

interface MessageHandler {
    fun onMessageReceived(context: ChatContext?, bundle: MessageBundle?)
}