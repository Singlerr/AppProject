package io.github.eh.eh.misc

import java.io.Serializable

class FriendChatInfo(
    val img: ByteArray,
    val name: String,
    val nickName: String,
    val id: String,
    val sex: String,
    val age: Int,
    val time: String,
    val ownerId: String
) : Serializable