package com.github.seclerp.bbsplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

object BbsScriptUtils {
    fun scriptExists(project: Project): Boolean {
        val monorepoRoot = BbsPaths.tryResolveMonorepoRoot(project) ?: return false
        return monorepoRoot.resolve(BbsPaths.bbsCmd.pathString).exists()
    }
}