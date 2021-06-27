package io.github.eh.eh.serverside

import java.sql.Date

class User {
    /**
     * A user id
     */
    val userId: String? = null

    /**
     * A real name of a user, not a nick name.
     */
    val name: String? = null

    /**
     * A nick name of a user.
     */
    val nickName: String? = null

    /**
     * A birth day of a user.
     */
    val birthDay: Date? = null

    /**
     * A sex of a user. It can be parsed as enum.
     *
     * @see Sex
     */
    val sex: String? = null

    /**
     * A image of a profile of user.
     * It is saved with type "Blob"
     */
    val image: ByteArray? = null

    /**
     * A set of interests that a user has.
     * All interests are transformed from Korean to English in app.
     */
    val interests: List<String>? = null

    /**
     * A form of this is not set.
     * TODO("Set a form of phone number")
     */
    val phoneNumber: String? = null

    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * You should set gps data manually to a gps data from rest api.
     */
    val gpsData: GPSData? = null
    val sexType: Sex
        get() = Sex.valueOf(sex!!)
}