package io.github.eh.eh.serverside.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.eh.eh.serverside.GPSData
import java.io.IOException

class GPSDataDeserializer : JsonDeserializer<GPSData?>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(
        jsonParser: JsonParser,
        deserializationContext: DeserializationContext
    ): GPSData {
        return ObjectMapper().readValue(jsonParser.valueAsString, GPSData::class.java)
    }
}