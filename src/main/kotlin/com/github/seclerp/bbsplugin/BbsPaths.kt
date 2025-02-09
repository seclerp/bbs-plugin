package com.github.seclerp.bbsplugin

import com.intellij.openapi.util.SystemInfo
import kotlin.io.path.Path

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
        SystemInfo.isMac -> Path(System.getProperty("user.home")).resolve("Library/Application Data")
        else -> Path(System.getProperty("user.home")).resolve(".local/share")
    }
}