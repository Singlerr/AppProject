package io.github.eh.eh.serverside

import java.io.Serializable


class DesiredTarget : Serializable {

    var user: User? = null
    var desiredAgeScope = 0
    var desiredSexScope = 0

    var gpsData: GPSData? = null
}
