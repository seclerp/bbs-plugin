package com.github.seclerp.bbsplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

/**
 * All paths are either absolute or relative to the monorepo Git root directory.
 */
object BbsPaths {
    val bbsCmd = Path("dotnet").resolve("bbs.cmd")
    val userSettingsFile get() = bbsAppDataFolder.resolve("settings.user.json")
    val userProfilesDirectory get() = bbsAppDataFolder.resolve("profiles")

    val bbsAppDataFolder get() = userAppDataFolder.resolve("JetBrains").resolve("Bbs")
    private val userAppDataFolder get() = when {
        SystemInfo.isWindows -> Path(System.getenv("APPDATA")!!)
        SystemInfo.isMac -> Path(System.getProperty("user.home")).resolve("Library/Application Support")
        else -> Path(System.getProperty("user.home")).resolve(".local/share")
    }

    fun resolveScriptPath(project: Project): Path =
        tryResolveScriptPath(project) ?: throw RuntimeException("BBS script expected to exist")

    fun resolveMonorepoRoot(project: Project): Path =
        tryResolveMonorepoRoot(project) ?: throw RuntimeException("No monorepo root found for project")

    fun tryResolveMonorepoRoot(project: Project): Path? = ProjectLevelVcsManager
        .getInstance(project)
        .allVcsRoots
        .map { it.path.toNioPath() }
        .firstOrNull { it.resolve(BbsPaths.bbsCmd.pathString).exists() }

    fun tryResolveScriptPath(project: Project): Path? {
        val monorepoRoot = tryResolveMonorepoRoot(project) ?: return null
        val scriptPath = monorepoRoot.resolve(BbsPaths.bbsCmd.pathString)
        return if (scriptPath.exists()) scriptPath else null
    }
}