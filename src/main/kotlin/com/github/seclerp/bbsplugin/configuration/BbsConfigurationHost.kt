package com.github.seclerp.bbsplugin.configuration

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.LocalFileSystem

class BbsConfigurationHost : ProjectActivity {
    override suspend fun execute(project: Project) {
        LocalFileSystem.getInstance().refreshAndFindFileByPath(project.basePath!!)
    }
}