package io.github.eh.eh.netty

import io.github.eh.eh.serverside.DesiredTarget
import io.github.eh.eh.serverside.User

class UserWrapper private constructor(
    val user: User,
    val callback: MatchingCallback,
    val desiredTarget: DesiredTarget
) {
    companion object {
        fun getInstance(
            user: User,
            callback: MatchingCallback,
            desiredTarget: DesiredTarget
        ): UserWrapper {
            return UserWrapper(user, callback, desiredTarget)
        }
    }
}