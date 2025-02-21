package com.github.seclerp.bbsplugin.actions

import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import javax.swing.MenuSelectionManager

class BbsSetProfileAction(
    private val profile: String,
    private val selected: Boolean,
    private val configHost: BbsConfigurationHost
) : ToggleAction(profile) {
    override fun isSelected(event: AnActionEvent) = selected

    override fun setSelected(event: AnActionEvent, currentSelected: Boolean) {
        configHost.setSelectedProfile(profile)
        // To close selection popup when choice has been made
        if (event.isFromMainMenu)
            MenuSelectionManager.defaultManager().clearSelectedPath()
    }
}