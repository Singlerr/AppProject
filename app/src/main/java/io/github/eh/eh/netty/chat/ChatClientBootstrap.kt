package io.github.eh.eh.netty.chat

import android.util.Log
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.netty.ChannelCallback
import io.github.eh.eh.netty.MessageDecoder
import io.github.eh.eh.netty.MessageEncoder
import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.DesiredTarget
import io.github.eh.eh.serverside.User
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelId
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.concurrent.GlobalEventExecutor


class ChatClientBootstrap private constructor(private val host: String, val port: Int) {
    private lateinit var messageHandler: ChatClientHandler
    private var future: ChannelFuture? = null
    var channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    @Throws(InterruptedException::class)
    fun startConnection(msgHandler: MessageHandler, channelCallback: ChannelCallback) {
        channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
        messageHandler = ChatClientHandler.getInstance(msgHandler, channels)
        val bootstrap = Bootstrap()
        val group: EventLoopGroup = NioEventLoopGroup(3)
        bootstrap.group(group)
            .channel(NioSocketChannel::class.java)
            .handler(LoggingHandler(LogLevel.INFO))
            .handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(IdleStateHandler(60, 30, 0))
                    ch.pipeline().addLast(
                        MessageDecoder(
                            arrayListOf(
                                DesiredTarget::class.java,
                                User::class.java,
                                ResponseBundle::class.java,
                                MessageBundle::class.java
                            )
                        ),
                        MessageEncoder(),
                        messageHandler
                    )
                }
            })
        future = bootstrap.connect(host, port).sync()
        channelCallback.onChannelInitialized(future!!.channel(),channels)
        future!!.channel().closeFuture().sync()
    }

    fun closeConnection() {
        future!!.channel().close()
        channels.close().awaitUninterruptibly()
    }

    companion object {
        fun getInstance(host: String, port: Int): ChatClientBootstrap {
            return ChatClientBootstrap(host, port)
        }
    }
}