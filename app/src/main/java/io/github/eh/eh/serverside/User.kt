package io.github.eh.eh.serverside

import android.net.Uri
import com.fasterxml.jackson.annotation.JsonIgnore
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.sql.Date
import java.time.LocalDate

class User : Serializable {
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
     * A set of friends.
     */
    val friends: List<String>? = null

    /**
     * A birth day of a user.
     **/
    var birthDay: LocalDate? = null

    /**
     * A age of a user.
     */
    var age: Int? = null

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
    var image: Uri? = null

    /**
     * A json of interests that a user has.
     * All interests are transformed from Korean to English in app.
     */
    var interests: String? = null

    /**
     * A interests in food of user.
     */
    var foodInterests: MutableSet<String>? = null

    /**
     * A interests in hobby of user.
     */
    var hobbyInterests: MutableSet<String>? = null

    /**
     * A interests in hobby of user.
     */
    var placeInterests: MutableSet<String>? = null

    /**
     * A form of this is not set.
     * TODO("Set a form of phone number")
     */
    var phoneNumber: String? = null

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

    @JsonIgnore
    var sexType: Sex? = null
        get() = Sex.valueOf(sex!!)

    @JsonIgnore
    var parseInterest: JSONObject? = null
        get() = try {
            JSONObject(interests)
        } catch (e: JSONException) {
            null
        }
    fun setSex(sexType:Sex){
        sex = sexType.name
    }
    fun setInterests(json: JSONObject?) {
        interests = json?.toString() ?: ""
    }
}