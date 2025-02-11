package com.github.seclerp.bbsplugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.util.applyIf
import kotlin.io.path.pathString

class BbsCommandLineBuilder(private val project: Project, private val entryPoint: String) {
    private var profile = "."
    private var additionalArguments = StringBuilder()

    fun withProfile(profile: String): BbsCommandLineBuilder {
        this.profile = profile
        return this
    }

    fun withArguments(arguments: String): BbsCommandLineBuilder {
        if (arguments.trim().isEmpty()) return this

        additionalArguments.append(" ", arguments)

        return this
    }

    fun build(): GeneralCommandLine {
        return GeneralCommandLine(BbsPaths.resolveScriptPath(project).pathString)
            .withParameters(entryPoint)
            .withParameters(profile)
            .applyIf(additionalArguments.isNotBlank()) {
                withParameters(additionalArguments.trimStart().toString())
            }
            .withCharset(Charsets.UTF_8)
            .withWorkDirectory(BbsPaths.resolveMonorepoRoot(project).pathString)
    }
}