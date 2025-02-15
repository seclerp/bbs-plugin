package com.github.seclerp.bbsplugin.configuration

import com.github.seclerp.bbsplugin.BbsPaths
import com.github.seclerp.bbsplugin.JsonSerializer
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.readText
import com.intellij.openapi.vfs.writeText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import java.nio.file.Path
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class BbsConfigurationHost(private val scope: CoroutineScope) {
    companion object {
        fun getInstance(project: Project) = project.service<BbsConfigurationHost>()
    }

    private val vfs by lazy { VirtualFileManager.getInstance() }

    val profiles = AtomicProperty(listOf<String>())
    val selectedProfilesPerProject = AtomicProperty(mapOf<String, String>())
    val entryPoints = AtomicProperty(BbsEntryPoints.allEntryPoints.toList())
    val selectedEntryPoints = AtomicProperty(listOf<String>())

    fun setSelectedProfile(repositoryRoot: Path, profile: String) {
        modifyJsonFile(BbsPaths.userSettingsFile) { json ->
            val selectedProfile = (json["SelectedProfile"]?.jsonObject?.toMutableMap() ?: mutableMapOf()).apply {
                put(repositoryRoot.pathString, JsonPrimitive(profile))
            }

            JsonObject(json.toMutableMap().apply {
                put("SelectedProfile", JsonObject(selectedProfile))
            })
        }
    }

    fun setSelectedEntryPoints(selectedEntryPoints: List<String>) {
        modifyJsonFile(BbsPaths.userSettingsFile) { json ->
            val selectedEntryPoints = JsonArray(selectedEntryPoints.map { JsonPrimitive(it) })

            JsonObject(json.toMutableMap().apply {
                put("EntryPoints", selectedEntryPoints)
            })
        }
    }

    @Suppress("UnstableApiUsage")
    private fun modifyJsonFile(path: Path, modifier: (JsonObject) -> JsonObject) {
        scope.launch(Dispatchers.EDT) {
            writeAction {
                val file = vfs.findFileByNioPath(BbsPaths.userSettingsFile) ?: return@writeAction
                val json = JsonSerializer.deserialize(file.readText())
                val modifiedJson = modifier(json)
                file.writeText(JsonSerializer.serialize(modifiedJson))
            }
        }
    }
}