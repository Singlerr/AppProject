package io.github.eh.eh.misc

import io.github.eh.eh.serverside.User
import java.io.Serializable

class FriendChatInfo(
    val img: ByteArray,
    val name: String,
    val id: String,
    val sex: String,
    val age: Int,
    val time: String,
    val user: User
) : Serializable