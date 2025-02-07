package com.github.seclerp.bbsplugin.settings

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.serialization.Serializable

@Service(Service.Level.PROJECT)
class BbsSettingsFileManager {
    companion object {
        fun getInstance(project: Project) = project.service<BbsSettingsFileManager>()
    }

    fun loadFile(path: VirtualFile): BbsSettingsFile {}

    fun saveFile(path: VirtualFile, settingsFile: BbsSettingsFile) {}
}

@Serializable
data class BbsSettingsFile(
    val selectedProfiles: MutableMap<String, String>,
)