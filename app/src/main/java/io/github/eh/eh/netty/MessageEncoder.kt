package io.github.eh.eh.netty

import io.github.eh.eh.Env
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class MessageEncoder : MessageToByteEncoder<Any?>() {
    @Throws(Exception::class)
    override fun encode(channelHandlerContext: ChannelHandlerContext, o: Any?, byteBuf: ByteBuf) {
        val data = Env.getMapper().writeValueAsBytes(o)
        byteBuf.writeBytes(data)
    }
}