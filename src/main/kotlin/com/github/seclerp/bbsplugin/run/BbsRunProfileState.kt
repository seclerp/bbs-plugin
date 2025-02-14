package com.github.seclerp.bbsplugin.run

import com.github.seclerp.bbsplugin.BbsCommandLineBuilder
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.AppIcon
import com.intellij.ui.SystemNotifications
import com.intellij.util.applyIf
import com.intellij.util.ui.UIUtil

class BbsRunProfileState(
    private val configuration: BbsRunConfiguration,
    environment: ExecutionEnvironment
) : CommandLineState(environment) {

    private val logger = logger<BbsRunProfileState>()
    private val escapeDecodes = AnsiEscapeDecoder()

    override fun startProcess(): ProcessHandler {
        val profile = configuration.profile?.trim() ?: ""
        val additionalArguments = configuration.additionalArguments?.trim() ?: ""
        val entryPoint = configuration.entryPoint?.trim() ?: ""

        val cmdBuilder = BbsCommandLineBuilder(configuration.project, entryPoint)
            .applyIf(profile != "") { withProfile(profile) }
            .applyIf(additionalArguments != "") { withArguments(additionalArguments) }

        return ColoredProcessHandler(cmdBuilder.build()).apply {
            addProcessListener(
                object : ProcessAdapter() {
                    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                        escapeDecodes.escapeText(event.text.trim(), outputType) { text, type ->
                            when (outputType) {
                                ProcessOutputTypes.STDOUT -> logger.info("bbs.cmd - stdout: ${text}")
                                ProcessOutputTypes.SYSTEM -> logger.info("bbs.cmd - system: ${text}")
                                ProcessOutputTypes.STDERR -> logger.warn("bbs.cmd - stderr: ${text}")
                                else -> logger.info("bbs.cmd - unknown: ${text}")
                            }
                        }
                    }

                    override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {
                        logger.debug("bbs.cmd - GOING TO BE TERMINATED: destroy - $willBeDestroyed\nhandler - ${event.processHandler}\nexitCode - ${event.exitCode}")
                    }

                    override fun processTerminated(event: ProcessEvent) {
                        logger.debug("bbs.cmd - TERMINATED: handler - ${event.processHandler}\nexitCode - ${event.exitCode}")
                        notifyBbsBuildFinished(environment.project, entryPoint, event.exitCode == 0)
                    }

                    override fun startNotified(event: ProcessEvent) {
                        logger.info("bbs.cmd - started")
                    }

                    override fun processNotStarted() {
                        logger.info("bbs.cmd - NOT STARTED")
                    }
                }
            )
            ProcessTerminatedListener.attach(this, environment.project)
        }
    }

    private fun notifyBbsBuildFinished(project: Project, entryPoint: String, success: Boolean) {
        val frame = WindowManager.getInstance().getFrame(project)
        if (frame != null && !frame.hasFocus()) {
            SystemNotifications.getInstance().notify("BBS", "BBS Build", if (success) "$entryPoint succeeded" else "$entryPoint failed")
        }

        UIUtil.invokeLaterIfNeeded {
            val appIcon = AppIcon.getInstance()
            if (!success) {
                appIcon.setErrorBadge(project, "1")
                appIcon.requestAttention(project, true)
            } else {
                appIcon.setOkBadge(project, true)
                appIcon.requestAttention(project, false)
            }
        }
    }
}