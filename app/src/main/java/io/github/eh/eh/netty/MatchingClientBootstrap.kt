package io.github.eh.eh.netty

import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.netty.MatchingClientHandler.Companion.getInstance
import io.github.eh.eh.serverside.DesiredTarget
import io.github.eh.eh.serverside.User
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.IdleStateHandler

class MatchingClientBootstrap private constructor(private val host: String, private val port: Int) {
    fun submit(user: UserWrapper?) {
        val bootstrap = Bootstrap()
        val group: EventLoopGroup = NioEventLoopGroup(3)
        val handler = getInstance(user!!)
        try {
            bootstrap.group(group)
                .channel(NioSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.AUTO_READ, true)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(IdleStateHandler(60, 30, 0))
                        ch.pipeline().addLast(
                            MessageDecoder(
                                arrayListOf(
                                    DesiredTarget::class.java,
                                    User::class.java,
                                    ResponseBundle::class.java
                                )
                            ),
                            MessageEncoder(),
                            handler
                        )
                    }
                })
            val future = bootstrap.connect(host, port).sync()
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