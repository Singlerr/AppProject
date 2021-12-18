package io.github.eh.eh.netty.chat

import io.github.eh.eh.netty.chat.bundle.MessageBundle
import io.github.eh.eh.serverside.User
import io.netty.channel.ChannelId
import io.netty.channel.group.ChannelGroup

class ChatContext private constructor(
    private val channels: ChannelGroup,
    private val id: ChannelId
) {

    lateinit var chatRoom: ChatRoom

    fun writeAndFlush(o: Any) {
        channels.find(id).writeAndFlush(o)
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