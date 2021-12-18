package io.github.eh.eh.netty

import io.netty.channel.Channel
import io.netty.channel.group.ChannelGroup

interface ChannelCallback {
    fun onChannelInitialized(channel: Channel, channels: ChannelGroup)
}