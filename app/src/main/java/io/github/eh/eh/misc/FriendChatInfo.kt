package io.github.eh.eh.misc

import java.io.Serializable

class FriendChatInfo(
    val img: ByteArray,
    val name: String,
    val sex: String,
    val age: Int,
    val time: String
) : Serializable