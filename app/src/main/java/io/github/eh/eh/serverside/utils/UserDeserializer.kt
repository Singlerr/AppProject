package io.github.eh.eh.serverside.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.eh.eh.serverside.User
import java.io.IOException

class UserDeserializer : JsonDeserializer<User?>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(
        jsonParser: JsonParser,
        deserializationContext: DeserializationContext
    ): User {
        return ObjectMapper().readValue(jsonParser.valueAsString, User::class.java)
    }
}