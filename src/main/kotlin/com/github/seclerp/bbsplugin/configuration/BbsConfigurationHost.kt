package com.github.seclerp.bbsplugin.configuration

import com.github.seclerp.bbsplugin.BbsPaths
import com.github.seclerp.bbsplugin.settings.BbsUserSettingsManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.util.containers.with
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class BbsConfigurationHost(private val scope: CoroutineScope) {
    companion object {
        fun getInstance(project: Project) = project.service<BbsConfigurationHost>()
    }

    private val vfs by lazy { VirtualFileManager.getInstance() }

    val selectedProfilesPerProject = AtomicProperty(mapOf<String, String>())
    val profiles = AtomicProperty(listOf<String>())
    val entryPoints = AtomicProperty(BbsEntryPoints.allEntryPoints.toList())

    @Suppress("UnstableApiUsage")
    fun setSelectedProfile(repositoryRoot: Path, profile: String) {
        scope.launch(Dispatchers.EDT) {
            writeAction {
                val virtualFile = vfs.findFileByNioPath(BbsPaths.userSettingsFile) ?: return@writeAction
                val freshContents = BbsUserSettingsManager.load(virtualFile)
                val updatedContents = freshContents.copy(freshContents.selectedProfile?.with(repositoryRoot.pathString, profile))
                BbsUserSettingsManager.save(virtualFile, updatedContents)
            }
        }
    }
}