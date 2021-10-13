package io.github.eh.eh.serverside

import java.io.Serializable

enum class SexScope(val sexScope: Int) : Serializable {
    SCOPE_ALL(0),
    SCOPE_MALE(1),
    SCOPE_FEMALE(2)
}