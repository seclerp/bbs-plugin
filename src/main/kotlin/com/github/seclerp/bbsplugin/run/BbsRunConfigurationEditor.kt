package com.github.seclerp.bbsplugin.run

import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.github.seclerp.bbsplugin.run.components.BbsAdditionalArgumentsEditor
import com.github.seclerp.bbsplugin.run.components.BbsEntryPointEditor
import com.github.seclerp.bbsplugin.run.components.BbsProfileEditor
import com.intellij.execution.options.LifetimedSettingsEditor
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.lifetime.Lifetime
import javax.swing.JComponent

class BbsRunConfigurationEditor(private val project: Project) : LifetimedSettingsEditor<BbsRunConfiguration>() {
    private val configurationHost by lazy { BbsConfigurationHost.getInstance(project) }
    private val profile = AtomicProperty("")
    private val entryPoint = AtomicProperty("")
    private val additionalArguments = AtomicProperty("")

    override fun resetEditorFrom(configuration: BbsRunConfiguration) {
        profile.set(configuration.profile ?: "")
        entryPoint.set(configuration.entryPoint ?: "")
        additionalArguments.set(configuration.additionalArguments ?: "")
    }

    override fun applyEditorTo(configuration: BbsRunConfiguration) {
        configuration.profile = profile.get()
        configuration.entryPoint = entryPoint.get()
        configuration.additionalArguments = additionalArguments.get()
    }

    override fun createEditor(lifetime: Lifetime): JComponent {
        return panel {
            row("Profile") {
                cell(BbsProfileEditor(project, configurationHost.profiles))
                    .align(AlignX.FILL)
                    .bindText(profile)
            }
            row("Entry point") {
                cell(BbsEntryPointEditor(project, configurationHost.entryPoints))
                    .align(AlignX.FILL)
                    .bindText(entryPoint)
            }
            row("Additional arguments") {
                cell(BbsAdditionalArgumentsEditor.create(project, lifetime))
                    .align(AlignX.FILL)
                    .bindText(additionalArguments)
            }
        }
    }
}