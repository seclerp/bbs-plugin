package com.github.seclerp.bbsplugin.actions

import com.github.seclerp.bbsplugin.environment.BbsScriptUtils
import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.project.Project

class BbsActionGroup : ActionGroup() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val project = event.project
        event.presentation.isEnabledAndVisible = project != null && BbsScriptUtils.scriptExists(project)
    }

    override fun getChildren(event: AnActionEvent?): Array<out AnAction?> {
        val project = event?.project ?: return emptyArray()
        return buildList {
            addEntryPoints(project)
            add(Separator())
            add(BbsSelectEntryPointsAction(project))
            add(BbsProfilesActionGroup(project))
        }.toTypedArray()
    }

    private fun MutableList<AnAction>.addEntryPoints(project: Project) {
        addAll(BbsConfigurationHost.getInstance(project).selectedEntryPoints
            .get()
            .map(::BbsLaunchEntryPointAction)
        )
    }
}

