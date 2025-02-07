package com.github.seclerp.bbsplugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

class BbsCommandLineBuilder(private val project: Project, private val entryPoint: String) {
    private var profile = "."

    fun withProfile(profile: String): BbsCommandLineBuilder {
        this.profile = profile
        return this
    }

    fun build(): GeneralCommandLine {
        return GeneralCommandLine(BbsScriptUtils.resolveScriptPath(project).path)
            .withParameters(entryPoint)
            .withParameters(profile)
    }
}