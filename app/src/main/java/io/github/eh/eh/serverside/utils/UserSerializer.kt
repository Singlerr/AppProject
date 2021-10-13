package io.github.eh.eh.serverside.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.eh.eh.serverside.User

class UserSerializer : JsonSerializer<User?>() {

    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value Value to serialize; can **not** be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     * serializing Objects value contains, if any.
     */
    override fun serialize(value: User?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen!!.writeObject(value)
    }
}