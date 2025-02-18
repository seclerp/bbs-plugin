package com.github.seclerp.bbsplugin.execution

import com.github.seclerp.bbsplugin.environment.BbsPaths
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.util.applyIf
import kotlin.io.path.pathString

class BbsCommandLineBuilder(private val project: Project, private val entryPoint: String) {
    private var profile = "."
    private var additionalArguments = StringBuilder()

    fun setProfile(profile: String) {
        this.profile = profile
    }

    fun addArguments(arguments: String) {
        if (arguments.trim().isEmpty()) return

        additionalArguments.append(" ", arguments)
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

internal fun buildBbsCommand(project: Project, entryPoint: String, predicate: BbsCommandLineBuilder.() -> Unit): GeneralCommandLine {
    return BbsCommandLineBuilder(project, entryPoint).let {
        it.predicate()
        it.build()
    }
}