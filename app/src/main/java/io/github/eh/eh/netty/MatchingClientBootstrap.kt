package io.github.eh.eh.netty

import io.github.eh.eh.netty.MatchingClientHandler.Companion.getInstance
import io.netty.bootstrap.Bootstrap
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

class MatchingClientBootstrap private constructor(private val host: String, private val port: Int) {
    fun submit(user: UserWrapper?) {
        val bootstrap = Bootstrap()
        val group: EventLoopGroup = NioEventLoopGroup(3)
        val handler = getInstance(user!!)
        try {
            bootstrap.group(group)
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .handler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ObjectDecoder(1024*1024,ClassResolvers.weakCachingConcurrentResolver(javaClass.classLoader)))
                        ch.pipeline().addLast(handler)
                        ch.pipeline().addLast(ObjectEncoder())
                    }
                })
            val future = bootstrap.connect(host,port).sync();
            future.channel().closeFuture().sync()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            try {
                group.shutdownGracefully().sync()
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        }
    }

    companion object {
        private var instance: MatchingClientBootstrap? = null
        fun getInstance(host: String, port: Int): MatchingClientBootstrap? {
            return if (instance == null) MatchingClientBootstrap(
                host,
                port
            ).also { instance = it } else instance
        }
    }
}