package com.github.seclerp.bbsplugin.run

import com.github.seclerp.bbsplugin.BbsCommandLineBuilder
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

class BbsRunProfileState(
    private val configuration: BbsRunConfiguration,
    environment: ExecutionEnvironment
) : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val cmdBuilder = BbsCommandLineBuilder(configuration.project, configuration.entryPoint!!)
        val profile = configuration.profile?.trim() ?: ""
        if (profile != "") cmdBuilder.withProfile(profile)
        return ColoredProcessHandler(cmdBuilder.build())
    }
}