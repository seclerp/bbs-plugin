package com.github.seclerp.bbsplugin.actions

import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project

class BbsProfilesActionGroup(
    private val project: Project
) : ActionGroup("Set Profile...", true), DumbAware {
    private val configHost by lazy { BbsConfigurationHost.getInstance(project) }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun getChildren(event: AnActionEvent?): Array<out AnAction?> {
        val profiles = configHost.profiles.get()

        return profiles.map { profile ->
            BbsSetProfileAction(profile, profile == configHost.selectedProfile.get(), configHost)
        }.toTypedArray()
    }
}