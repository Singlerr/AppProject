package io.github.eh.eh.serverside

import java.io.Serializable
import java.sql.Date

class User : Serializable{
    /**
     * A user id
     **/
    var userId: String? = null

    /**
     * A user password
     */
    var password: String? = null

    /**
     * A real name of a user, not a nick name.
     **/
    var name: String? = null

    /**
     * A nick name of a user.
     **/
    var nickName: String? = null

    /**
     * A birth day of a user.
     **/
    var birthDay: Date? = null

    /**
     * A sex of a user. It can be parsed as enum.
     *
     * @see Sex
     */
    var sex: String? = null

    /**
     * A image of a profile of user.
     * It is saved with type "Blob"
     */
    var image: ByteArray? = null

    /**
     * A set of interests that a user has.
     * All interests are transformed from Korean to English in app.
     */
    var interests: List<String>? = null

    /**
     * A form of this is not set.
     * TODO("Set a form of phone number")
     */
    var phoneNumber: String? = null

    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * You should set gps data manually to a gps data from rest api.
     */
    var gpsData: GPSData? = null

    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * It contains result message from server when register/login operation occurred.
     */
    val result: String? = null

    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * It contains responseCode from server when register/login operation occurred.
     */
    val responseCode = 0

    var sexType: Sex? = null
        get() = Sex.valueOf(sex!!)
}