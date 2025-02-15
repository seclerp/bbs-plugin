package com.github.seclerp.bbsplugin.settings

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.readText
import com.intellij.util.concurrency.annotations.RequiresReadLock
import com.intellij.util.concurrency.annotations.RequiresWriteLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object BbsUserSettingsManager {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        prettyPrintIndent = "  "
    }
    private val serializer = BbsUserSettingsFile.serializer()
    @RequiresReadLock
    fun load(file: VirtualFile): BbsUserSettingsFile = json.decodeFromString(serializer, file.readText())
}