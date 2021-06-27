package io.github.eh.eh.netty

import io.github.eh.eh.serverside.User

class UserWrapper private constructor(
    val user: User,
    val callback: MatchingCallback
) {
    companion object {
        fun getInstance(user: User, callback: MatchingCallback): UserWrapper {
            return UserWrapper(user, callback)
        }
    }
}