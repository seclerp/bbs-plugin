package com.github.seclerp.bbsplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

object BbsScriptUtils {
    fun scriptExists(project: Project): Boolean =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .any { it.path.toNioPath().resolve(BbsPaths.bbsCmd.pathString).exists() == true }

    fun resolveScriptPath(project: Project): Path =
        tryResolveScriptPath(project) ?: throw RuntimeException("BBS script expected to exist")

    fun tryResolveScriptPath(project: Project): Path? =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .map { it.path.toNioPath().resolve(BbsPaths.bbsCmd.pathString) }
            .firstOrNull { it.exists() == true }
}