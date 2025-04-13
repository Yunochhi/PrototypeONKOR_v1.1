package com.example.prototypeonkor.Classes.Serializers

import android.icu.text.ListFormatter
import android.icu.text.MessagePattern
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.lang.reflect.Type

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        return LocalDateTime.parse(
            json.asString,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )
    }

    override fun serialize(
        src: LocalDateTime,
        type: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(
            src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}