package io.github.eh.eh.serverside

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
class DesiredTarget : Serializable {

    var user: User? = null
    var desiredAgeScope = 0
    var desiredSexScope = 0

    var gpsData: GPSData? = null
}
