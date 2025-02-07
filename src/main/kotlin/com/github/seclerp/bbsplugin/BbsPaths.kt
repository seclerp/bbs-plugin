package com.github.seclerp.bbsplugin

import com.intellij.openapi.util.SystemInfo
import kotlin.io.path.Path

/**
 * All paths are either absolute or relative to the monorepo Git root directory.
 */
object BbsPaths {
    val BBS_CMD = Path("dotnet/bbs.cmd")
    val BBS_USER_SETTINGS_FILE get() = BBS_APPDATA_FOLDER.resolve("IntelliJBBS").resolve("settings.json")

    val BBS_APPDATA_FOLDER get() = USER_APPDATA_FOLDER.resolve("JetBrains").resolve("Bbs")

    private val USER_APPDATA_FOLDER get() = when {
        SystemInfo.isWindows -> Path(System.getenv("APPDATA")!!)
        SystemInfo.isMac -> Path(System.getProperty("user.home")).resolve("Library/Application Data")
        else -> Path(System.getProperty("user.home")).resolve("/.local/share")
    }
}