package com.github.seclerp.bbsplugin.configuration

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlinx.coroutines.CoroutineScope

class BbsFileWatcherActivity(private val scope: CoroutineScope) : ProjectActivity {
    override suspend fun execute(project: Project) {
        BbsFileWatcher(project, scope).launch()
    }
}