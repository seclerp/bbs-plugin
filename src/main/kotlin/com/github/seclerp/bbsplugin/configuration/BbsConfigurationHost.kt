package com.github.seclerp.bbsplugin.configuration

import com.github.seclerp.bbsplugin.environment.BbsPaths
import com.github.seclerp.bbsplugin.JsonSerializer
import com.github.seclerp.bbsplugin.environment.BbsProfiles
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
class BbsConfigurationHost(private val project: Project, private val scope: CoroutineScope) {
    companion object {
        fun getInstance(project: Project) = project.service<BbsConfigurationHost>()
    }

    private val vfs by lazy { VirtualFileManager.getInstance() }

    val profiles = AtomicProperty(listOf<String>())
    val selectedProfilesPerProject = AtomicProperty(mapOf<Path, String>())
    val selectedProfile = AtomicProperty(BbsProfiles.Default)
    val entryPoints = AtomicProperty(BbsEntryPoints.allEntryPoints.toList())
    val selectedEntryPoints = AtomicProperty(listOf<String>())

    init {
        selectedProfilesPerProject.afterChange { value ->
            val monorepoRoot = BbsPaths.tryResolveMonorepoRoot(project) ?: return@afterChange
            val selected = value[monorepoRoot]
            if (selected != null)
                selectedProfile.set(selected)
            else
                selectedProfile.set(BbsProfiles.Default)
        }
    }

    fun setSelectedProfile(profile: String) {
        val monorepoRoot = BbsPaths.tryResolveMonorepoRoot(project) ?: return
        modifyJsonFile(BbsPaths.userSettingsFile) { json ->
            val selectedProfile = (json["SelectedProfile"]?.jsonObject?.toMutableMap() ?: mutableMapOf()).apply {
                put(monorepoRoot.pathString, JsonPrimitive(profile))
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
                val file = vfs.findFileByNioPath(path) ?: return@writeAction
                val json = JsonSerializer.deserialize(file.readText())
                val modifiedJson = modifier(json)
                file.writeText(JsonSerializer.serialize(modifiedJson))
            }
        }
    }
}