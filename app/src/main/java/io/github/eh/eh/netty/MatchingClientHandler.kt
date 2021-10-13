package io.github.eh.eh.netty

import android.util.Log
import io.github.eh.eh.netty.utils.ObjectSerializer
import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

class MatchingClientHandler private constructor(private val user: UserWrapper) :
    ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        var cf = ctx.writeAndFlush(ObjectSerializer.writeJsonAsByteBuf(user.desiredTarget))!!
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is User) {
            user.callback.onMatched(user, msg)
        }
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.READER_IDLE) {
                //No connection
                ctx!!.close()
            } else if (evt.state() == IdleState.WRITER_IDLE) {
                ctx!!.writeAndFlush("pong")
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        Log.e("ERROR", cause!!.toString())
    }

    companion object {
        @JvmStatic
        fun getInstance(user: UserWrapper): MatchingClientHandler {
            return MatchingClientHandler(user)
        }
    }
}