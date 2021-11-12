package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelId
import io.netty.channel.group.ChannelGroup

class ChatContext {

    private var channelGroup: ChannelGroup? = null
    private var channelId: ChannelId? = null
    var chatRoom: ChatRoom? = null

    private constructor(channels: ChannelGroup, id: ChannelId) {
        this.channelGroup = channels
        this.channelId = id
    }

    fun writeAndFlush(o: Any) {
        channelGroup!!.find(channelId!!).writeAndFlush(o)
    }

    companion object {

        fun getInstance(channelGroup: ChannelGroup, id: ChannelId): ChatContext {
            return ChatContext(channelGroup, id)
        }

        fun newMessage(msg: String, targetUserId: String, user: User, time: String): MessageBundle {

            return MessageBundle.createMessage(msg, targetUserId, user.userId!!, time)
        }
    }
}