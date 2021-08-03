package io.github.eh.eh.netty

import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class MatchingClientHandler private constructor(private val user: UserWrapper) :
    ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(user)
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is User) {
            user.callback.onMatched(user, msg)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(user: UserWrapper): MatchingClientHandler {
            return MatchingClientHandler(user)
        }
    }
}