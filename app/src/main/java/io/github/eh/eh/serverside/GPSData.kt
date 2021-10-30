package io.github.eh.eh.serverside

import java.io.Serializable


class GPSData : Serializable {
    /**
     * A latitude of gps data
     */
    private val latitude = 0.0

    /**
     * A longitude of gps data
     */
    private val longitude = 0.0

    /**
     * The id of the owner of this gps data.
     */
    private val id: String? = null
    override fun toString(): String {
        return "GPSData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", id='" + id + '\'' +
                '}'
    }
}