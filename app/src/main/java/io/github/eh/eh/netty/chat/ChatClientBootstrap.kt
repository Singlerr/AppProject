package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.ChatClientHandler
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import lombok.Getter

class ChatClientBootstrap private constructor(host: String, port: Int) {
    @Getter
    private var messageHandler: ChatClientHandler? = null
    private val host: String? = null
    private val port = 0
    private var future: ChannelFuture? = null
    @Throws(InterruptedException::class)
    fun startConnection(msgHandler: MessageHandler) {
        messageHandler = ChatClientHandler.Companion.getInstance(msgHandler)
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
                }
            })
        future = bootstrap.connect(host, port).sync()
        future!!.channel().closeFuture().sync()
    }

    fun closeConnection() {
        future!!.channel().close()
    }

    companion object {
        fun getInstance(host: String, port: Int): ChatClientBootstrap {
            return ChatClientBootstrap(host, port)
        }
    }
}