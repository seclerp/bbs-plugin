package com.github.seclerp.bbsplugin.run

import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.github.seclerp.bbsplugin.configuration.BbsEntryPoint
import com.github.seclerp.bbsplugin.toMutableProperty
import com.intellij.execution.options.LifetimedSettingsEditor
import com.intellij.openapi.externalSystem.service.ui.command.line.CommandLineField
import com.intellij.openapi.externalSystem.service.ui.command.line.CommandLineInfo
import com.intellij.openapi.externalSystem.service.ui.command.line.CompletionTableInfo
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionField
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionInfo
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionRenderer
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionRenderer.Cell
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionRenderer.Companion.append
import com.intellij.openapi.externalSystem.service.ui.completion.collector.TextCompletionCollector
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.lifetime.Lifetime
import javax.swing.JComponent
import javax.swing.SwingConstants

class BbsRunConfigurationEditor(private val project: Project) : LifetimedSettingsEditor<BbsRunConfiguration>() {
    private val configurationHost by lazy { BbsConfigurationHost.getInstance(project) }
    private val profile = AtomicProperty("")
    private val entryPoint = AtomicProperty("")

    override fun resetEditorFrom(configuration: BbsRunConfiguration) {
        profile.set(configuration.profile ?: "")
        entryPoint.set(configuration.entryPoint ?: "")
    }

    override fun applyEditorTo(configuration: BbsRunConfiguration) {
        configuration.profile = profile.get()
        configuration.entryPoint = entryPoint.get()
    }

    override fun createEditor(lifetime: Lifetime): JComponent {
        return panel {
            row("Profile") {
                cell(createProfileEditor())
                    .align(AlignX.FILL)
                    .bindText(profile)
            }
            row("Entry point") {
                cell(createEntryPointEditor())
                    .align(AlignX.FILL)
                    .bindText(entryPoint)
            }
        }
    }

    private fun createProfileEditor(): TextCompletionField<String> {
        return object : TextCompletionField<String>(project) {
            override val completionCollector = TextCompletionCollector.basic {
                configurationHost.profiles.get()
            }
        }
    }

    private fun createEntryPointEditor(): TextCompletionField<BbsEntryPoint> {
        return object : TextCompletionField<BbsEntryPoint>(project) {
            override val completionCollector = TextCompletionCollector.basic {
                configurationHost.entryPoints.get()
            }
        }.apply {
            renderer = object : TextCompletionRenderer<BbsEntryPoint> {
                override fun getText(item: BbsEntryPoint) = item.alias

                override fun customizeCellRenderer(
                    editor: TextCompletionField<BbsEntryPoint>,
                    cell: Cell<BbsEntryPoint>
                ) {
                    cell.component.append(getText(cell.item), editor.text)
                    cell.component.append(cell.item.description,
                        SimpleTextAttributes.GRAYED_ATTRIBUTES,
                        0,
                        SwingConstants.RIGHT)
                }
            }
        }
    }
}