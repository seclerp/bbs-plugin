package com.github.seclerp.bbsplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vfs.VirtualFile

object BbsScriptUtils {
    const val RELATIVE_SCRIPT_PATH = "dotnet/bbs.cmd"
    fun scriptExists(project: Project): Boolean =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .any { it.path.findFileByRelativePath(RELATIVE_SCRIPT_PATH)?.exists() == true }

    fun tryResolveScriptPath(project: Project): VirtualFile? =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .map { it.path.findFileByRelativePath(RELATIVE_SCRIPT_PATH) }
            .firstOrNull { it?.exists() == true }

    fun resolveScriptPath(project: Project): VirtualFile =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .map { it.path.findFileByRelativePath(RELATIVE_SCRIPT_PATH) }
            .firstOrNull { it?.exists() == true }
                ?: throw RuntimeException("BBS script expected to exist")
}