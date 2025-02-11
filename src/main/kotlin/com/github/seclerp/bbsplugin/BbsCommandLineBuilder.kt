package com.github.seclerp.bbsplugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import kotlin.io.path.pathString

class BbsCommandLineBuilder(private val project: Project, private val entryPoint: String) {
    private var profile = "."

    fun withProfile(profile: String): BbsCommandLineBuilder {
        this.profile = profile
        return this
    }

    fun build(): GeneralCommandLine {
        return GeneralCommandLine(BbsPaths.resolveScriptPath(project).pathString)
            .withParameters(entryPoint)
            .withParameters(profile)
    }
}