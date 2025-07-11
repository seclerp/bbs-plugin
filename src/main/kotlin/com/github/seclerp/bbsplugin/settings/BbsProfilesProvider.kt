package com.github.seclerp.bbsplugin.settings

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.isDirectory

object BbsProfilesProvider {
    fun loadProfiles(profilesFolder: Path): List<String> {
        val profiles = profilesFolder
            .toFile()
            .list { file, name ->
                val filePath = file.toPath().resolve(name)

                !filePath.isDirectory()
                    && filePath.exists()
                    && filePath.extension == "json"
            }
            ?.map { it.removeSuffix(".json") }
            ?.toMutableList() ?: mutableListOf()

        return profiles
    }
}