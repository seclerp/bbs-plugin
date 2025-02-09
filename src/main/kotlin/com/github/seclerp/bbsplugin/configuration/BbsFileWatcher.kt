package com.github.seclerp.bbsplugin.configuration

import com.github.seclerp.bbsplugin.BbsPaths
import com.github.seclerp.bbsplugin.settings.BbsProfilesProvider
import com.github.seclerp.bbsplugin.settings.BbsUserSettingsProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.collections.forEach
import kotlin.io.path.pathString

class BbsFileWatcher(private val project: Project, private val scope: CoroutineScope) {
    private val executor = OrderedTaskExecutor(scope)
    private val configurationHost by lazy { BbsConfigurationHost.getInstance(project) }

    fun launch() {
        includeBbsAppDataUpdates()
        // Initial update to fill up the observables
        executor.enqueue { updateUserSettingsFile() }
        executor.enqueue { updateProfiles() }

        project.messageBus
            .connect(scope)
            .subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
                override fun after(events: List<VFileEvent>) {
                    events.forEach(::handleVfsChange)
                }
            })
    }

    private fun includeBbsAppDataUpdates() {
        LocalFileSystem.getInstance().refreshAndFindFileByPath(BbsPaths.userSettingsFile.pathString)
        LocalFileSystem.getInstance().refreshAndFindFileByPath(BbsPaths.userProfilesDirectory.pathString)
    }

    private fun handleVfsChange(event: VFileEvent) {
        val path = event.file?.path?.toNioPathOrNull() ?: return
        when {
            path == BbsPaths.userSettingsFile -> executor.enqueue { updateUserSettingsFile() }
            path.startsWith(BbsPaths.userProfilesDirectory) -> executor.enqueue { updateProfiles() }
        }
    }

    private fun updateUserSettingsFile() {
        val updatedFile = BbsUserSettingsProvider.loadFile(BbsPaths.userSettingsFile)
        configurationHost.selectedProfilesPerProject.set(updatedFile.selectedProfile?.toMap() ?: emptyMap())
    }

    private fun updateProfiles() {
        val updatedProfiles = BbsProfilesProvider.loadProfiles(BbsPaths.userProfilesDirectory)
        configurationHost.profiles.set(updatedProfiles)
    }
}