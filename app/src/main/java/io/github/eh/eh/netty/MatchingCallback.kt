package io.github.eh.eh.netty

import io.github.eh.eh.serverside.User

interface MatchingCallback {
    fun onMatched(userWrapper: UserWrapper?, targetUser: User?)
}