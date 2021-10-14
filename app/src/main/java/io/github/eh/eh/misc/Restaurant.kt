package io.github.eh.eh.misc

import java.io.Serializable

class Restaurant(
    val name: String,
    val rate: Double,
    val category: String,
    val address: String,
    val image: ByteArray
) : Serializable