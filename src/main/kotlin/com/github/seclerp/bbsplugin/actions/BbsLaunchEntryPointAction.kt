package com.github.seclerp.bbsplugin.actions

import com.github.seclerp.bbsplugin.execution.BbsRunConfiguration
import com.github.seclerp.bbsplugin.execution.BbsRunConfigurationType
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class BbsLaunchEntryPointAction(
    private val entryPoint: String
) : AnAction(entryPoint, null, AllIcons.Actions.Execute), DumbAware {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val runManager = RunManager.getInstance(project)
        val settings = runManager.prepareInstantBbsConfiguration(entryPoint)

        ExecutionUtil.runConfiguration(settings, DefaultRunExecutor.getRunExecutorInstance())
    }

    private fun RunManager.prepareInstantBbsConfiguration(entryPoint: String): RunnerAndConfigurationSettings {
        clearInstantBbsConfigs()
        val settings = createInstantBbsConfig(entryPoint)
        selectedConfiguration = settings

        return settings
    }

    private fun RunManager.createInstantBbsConfig(entryPoint: String): RunnerAndConfigurationSettings {
        val settings = createConfiguration("BBS", BbsRunConfigurationType::class.java)
        settings.isTemporary = true

        val bbsConfiguration = settings.configuration as BbsRunConfiguration
        bbsConfiguration.entryPoint = entryPoint
        bbsConfiguration.name = "BBS: $entryPoint"

        addConfiguration(settings)
        return settings
    }

    private fun RunManager.clearInstantBbsConfigs() {
        val temporaryConfigurations = allSettings.filter { it.isTemporary && it.configuration is BbsRunConfiguration }
        temporaryConfigurations.forEach {
            removeConfiguration(it)
        }
    }
}