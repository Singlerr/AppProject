package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelHandlerContext

class ChatContext private constructor(private val context: ChannelHandlerContext) {
    fun sendMessage(msg: String,targetUserId: String,user: User) {
        val bundle: MessageBundle = MessageBundle.Companion.createMessage(msg,targetUserId,user)
        context.writeAndFlush(bundle)
    }

    companion object {
        fun getInstance(context: ChannelHandlerContext): ChatContext {
            return ChatContext(context)
        }
    }
}