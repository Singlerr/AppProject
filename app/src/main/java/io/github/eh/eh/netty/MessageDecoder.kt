package io.github.eh.eh.netty

import io.github.eh.eh.Env
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.json.JSONObject

class MessageDecoder(private val registeredClasses: List<Class<*>>) : ByteToMessageDecoder() {
    @Throws(Exception::class)
    override fun decode(
        channelHandlerContext: ChannelHandlerContext,
        byteBuf: ByteBuf,
        list: MutableList<Any>
    ) {
        val len = byteBuf.readableBytes()
        val read = ByteArray(len)
        for (i in 0 until len) {
            read[i] = byteBuf.getByte(i)
        }
        val obj = JSONObject(String(read))
        val clsName = obj.getString("@type")
        for (cls in registeredClasses) {
            if (cls.simpleName.equals(clsName, ignoreCase = true)) {
                val o = Env.getMapper().readValue(read, cls)
                list.add(o)
            }
        }
        byteBuf.clear()
    }
}