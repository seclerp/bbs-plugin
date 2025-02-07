package com.github.seclerp.bbsplugin.run

import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.options.SettingsEditor
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class BbsRunConfigurationEditor : SettingsEditor<BbsRunConfiguration>() {
    private val profileProperty = AtomicProperty("")
    private val entryPoint = AtomicProperty("")

    override fun resetEditorFrom(configuration: BbsRunConfiguration) {
        profileProperty.set(configuration.profile ?: "")
        entryPoint.set(configuration.entryPoint ?: "")
    }

    override fun applyEditorTo(configuration: BbsRunConfiguration) {
        configuration.profile = profileProperty.get()
        configuration.entryPoint = entryPoint.get()
    }

    override fun createEditor(): JComponent {
        return panel {
            row("Profile") {
                textField().bindText(profileProperty)
            }
            row("Entry point") {
                textField().bindText(entryPoint)
            }
        }
    }
}