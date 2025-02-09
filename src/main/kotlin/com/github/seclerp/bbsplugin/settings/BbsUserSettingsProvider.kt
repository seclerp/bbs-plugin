package com.github.seclerp.bbsplugin.settings

import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.readText

object BbsUserSettingsProvider {
    private val json = Json { ignoreUnknownKeys = true }
    fun loadFile(path: Path): BbsUserSettingsFile = json.decodeFromString(path.readText())
}