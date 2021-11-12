package io.github.eh.eh.netty

import android.util.Base64
import io.github.eh.eh.Env
import io.github.eh.eh.http.HttpStatus
import io.github.eh.eh.http.bundle.ResponseBundle
import io.github.eh.eh.http.cipher.CipherBase
import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import java.net.URL


class MatchingClientHandler private constructor(private val user: UserWrapper) :
    ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        var cf = ctx.writeAndFlush(user.desiredTarget)
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is ResponseBundle) {
            if (msg.responseCode == HttpStatus.SC_OK) {
                var u = msg.getMessage(User::class.java)
                u.image = Base64.decode(
                    getImage(String.format(Env.REQ_PROFILE_IMAGE, u.result!!)),
                    Base64.DEFAULT
                )
                user.callback.onMatched(user, u)
                ctx.channel().closeFuture().sync()
            }
        }
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.READER_IDLE) {
                //No connection
                ctx!!.close()
            } else if (evt.state() == IdleState.WRITER_IDLE) {
                ctx!!.writeAndFlush(PingMessage())
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
    }

    companion object {
        @JvmStatic
        fun getInstance(user: UserWrapper): MatchingClientHandler {
            return MatchingClientHandler(user)
        }
    }

    private fun getImage(url: String): String {
        var url = URL(url)
        var preInput = url.openStream()
        var r = CipherBase.instance!!.decode(preInput).reader().readText()
        return r
    }
}
