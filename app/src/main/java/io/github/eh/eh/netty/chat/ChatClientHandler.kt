package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.group.ChannelGroup

class ChatClientHandler : ChannelInboundHandlerAdapter {

    private var channelGroup: ChannelGroup? = null

    private var chatContext: ChatContext? = null
        get() {
            return chatContext
        }

    private var messageHandler: MessageHandler? = null

    private constructor(context: ChatContext) {
        chatContext = context
    }

    private constructor(messageHandler: MessageHandler, channels: ChannelGroup) {
        this.messageHandler = messageHandler
        this.channelGroup = channels
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        chatContext = ChatContext.getInstance(channelGroup!!, ctx.channel().id())
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is MessageBundle) messageHandler!!.onMessageReceived(chatContext!!, msg)
    }

    companion object {
        fun getInstance(context: ChatContext): ChatClientHandler {
            return ChatClientHandler(context)
        }

        fun getInstance(messageHandler: MessageHandler, channels: ChannelGroup): ChatClientHandler {
            return ChatClientHandler(messageHandler, channels)
        }
    }
}