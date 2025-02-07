package com.github.seclerp.bbsplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vfs.VirtualFile

object BbsScriptUtils {
    fun scriptExists(project: Project): Boolean =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .any { it.path.findFileByRelativePath(BbsPaths.BBS_CMD)?.exists() == true }

    fun tryResolveScriptPath(project: Project): VirtualFile? =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .map { it.path.findFileByRelativePath(BbsPaths.BBS_CMD) }
            .firstOrNull { it?.exists() == true }

    fun resolveScriptPath(project: Project): VirtualFile =
        ProjectLevelVcsManager
            .getInstance(project)
            .allVcsRoots
            .map { it.path.findFileByRelativePath(BbsPaths.BBS_CMD) }
            .firstOrNull { it?.exists() == true }
                ?: throw RuntimeException("BBS script expected to exist")
}