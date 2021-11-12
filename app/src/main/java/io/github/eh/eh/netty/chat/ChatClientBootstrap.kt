package io.github.eh.eh.netty.chat

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelId
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.util.concurrent.GlobalEventExecutor


class ChatClientBootstrap private constructor(host: String, port: Int) {
    private var messageHandler: ChatClientHandler? = null
        get() {
            return messageHandler
        }
    private val host: String? = null
    private val port = 0
    private var future: ChannelFuture? = null
    var channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    @Throws(InterruptedException::class)
    fun startConnection(msgHandler: MessageHandler): ChatContext {
        var chId: ChannelId? = null
        channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
        messageHandler = ChatClientHandler.getInstance(msgHandler, channels)
        val bootstrap = Bootstrap()
        val group: EventLoopGroup = NioEventLoopGroup(3)
        bootstrap.group(group)
            .channel(NioServerSocketChannel::class.java)
            .handler(LoggingHandler(LogLevel.INFO))
            .handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(
                        ObjectDecoder(
                            1024 * 1024,
                            ClassResolvers.weakCachingConcurrentResolver(javaClass.classLoader)
                        )
                    )
                    ch.pipeline().addLast(messageHandler)
                    ch.pipeline().addLast(ObjectEncoder())
                    channels.add(ch)
                }
            })
        future = bootstrap.connect(host, port).sync()
        chId = future!!.channel().id()
        future!!.channel().closeFuture().sync()
        return ChatContext.getInstance(channels, chId)
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