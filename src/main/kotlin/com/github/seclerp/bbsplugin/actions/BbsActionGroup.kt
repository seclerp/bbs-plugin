package com.github.seclerp.bbsplugin.actions

import com.github.seclerp.bbsplugin.BbsScriptUtils
import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class BbsActionGroup : ActionGroup() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val project = event.project
        event.presentation.isEnabledAndVisible = project != null && BbsScriptUtils.scriptExists(project)
    }

    override fun getChildren(event: AnActionEvent?): Array<out AnAction?> {
        val project = event?.project ?: return emptyArray()
        val selectedEntryPoints = BbsConfigurationHost.getInstance(project).selectedEntryPoints.get()
        return selectedEntryPoints.map(::BbsLaunchEntryPointAction).toTypedArray()
    }
}
