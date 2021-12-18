package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.group.ChannelGroup


class ChatClientHandler : ChannelInboundHandlerAdapter {

    private var channelGroup: ChannelGroup? = null


    var chatContext: ChatContext? = null

    private lateinit var ownerId: String
    private var messageHandler: MessageHandler? = null

    private constructor(context: ChatContext) {
        chatContext = context
    }

    private constructor(
        messageHandler: MessageHandler,
        channels: ChannelGroup,
        ownerId: String = "none"
    ) {
        this.messageHandler = messageHandler
        this.channelGroup = channels
        this.ownerId = ownerId
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        chatContext = ChatContext.getInstance(channelGroup!!, ctx.channel().id())
        ctx.writeAndFlush(
            MessageBundle.createMessage(
                message = "",
                targetUserId = "",
                ownerId = ownerId,
                time = "",
                state = "HANDSHAKE"
            )
        )
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is MessageBundle) messageHandler!!.onMessageReceived(chatContext!!, msg)
    }

    companion object {
        fun getInstance(context: ChatContext): ChatClientHandler {
            return ChatClientHandler(context)
        }

        fun getInstance(
            messageHandler: MessageHandler,
            channels: ChannelGroup,
            ownerId: String = "none"
        ): ChatClientHandler {
            return ChatClientHandler(messageHandler, channels, ownerId)
        }
    }
}