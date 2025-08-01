package com.github.seclerp.bbsplugin

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

object JsonSerializer {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
        ignoreUnknownKeys = true
    }

    fun serialize(obj: JsonObject) = json.encodeToString(obj)
    fun deserialize(contents: String) = json.parseToJsonElement(contents).jsonObject
}

