package com.github.seclerp.bbsplugin.run

import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.github.seclerp.bbsplugin.configuration.BbsEntryPoint
import com.github.seclerp.bbsplugin.toMutableProperty
import com.intellij.execution.options.LifetimedSettingsEditor
import com.intellij.execution.runToolbar.components.TrimmedMiddleLabel
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.JBColor
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBInsets
import com.intellij.util.ui.UIUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class BbsRunConfigurationEditor(project: Project) : LifetimedSettingsEditor<BbsRunConfiguration>() {
    private val configurationHost by lazy { BbsConfigurationHost.getInstance(project) }
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

    override fun createEditor(lifetime: Lifetime): JComponent {
        return panel {
            row("Profile") {
                cell(createProfileEditor(lifetime))
                    .align(AlignX.FILL)
                    .bindItem(profileProperty)
            }
            row("Entry point") {
                cell(createEntryPointEditor())
                    .align(AlignX.FILL)
                    .bind(
                        { it.editor.item as? String ?: "" },
                        { component, value -> component.setSelectedItem(value) },
                        entryPoint.toMutableProperty()
                    )
            }
        }
    }

    private fun createProfileEditor(lifetime: Lifetime): ComboBox<String> {
        val model = DefaultComboBoxModel<String>()
        configurationHost.profiles.get().forEach { profile ->
            model.addElement(profile)
        }

        configurationHost.profiles.afterChange(lifetime.createNestedDisposable()) { value ->
            model.removeAllElements()
            value.forEach { profile ->
                model.addElement(profile)
            }
        }

        return ComboBox(model).apply {
            isEditable = true
            renderer = SimpleListCellRenderer.create { label, value, _ ->
                label.text = value
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun createEntryPointEditor(): ComboBox<BbsEntryPoint> {
        val model = object : DefaultComboBoxModel<BbsEntryPoint>() {
            override fun setSelectedItem(anObject: Any?) {
                if (anObject is BbsEntryPoint) {
                    super.setSelectedItem(anObject.alias)
                } else {
                    super.setSelectedItem(anObject)
                }
            }
            override fun getSelectedItem(): Any? {
                val selectedItem = super.getSelectedItem()
                return if (selectedItem is BbsEntryPoint) {
                    selectedItem.alias
                } else {
                    selectedItem
                }
            }
        }
        configurationHost.entryPoints.get().forEach { entryPoint ->
            model.addElement(entryPoint)
        }

        return ComboBox(model).apply {
            setMinimumAndPreferredWidth(0)
            isSwingPopup = false
            isEditable = true
            renderer = object : ListCellRenderer<BbsEntryPoint> {
                private val aliasComponent = createLabelComponent().apply { foreground = JBColor.BLACK }
                private val titleComponent = createLabelComponent().apply { foreground = JBColor.GRAY }

                private val rowComponent = JPanel(GridBagLayout()).apply {
                    border = IdeBorderFactory.createEmptyBorder(insets)

                    add(aliasComponent, GridBagConstraints(
                        0, 0,
                        1, 1,
                        0.0, 0.0,
                        GridBagConstraints.BASELINE,
                        GridBagConstraints.NONE,
                        JBInsets.emptyInsets(),
                        0, 0
                    ))
                    add(titleComponent, GridBagConstraints(
                        1, 0,
                        1, 1,
                        1.0, 0.0,
                        GridBagConstraints.BASELINE,
                        GridBagConstraints.HORIZONTAL,
                        JBInsets.create(0, 10),
                        0, 0
                    ))
                }

                override fun getListCellRendererComponent(
                    list: JList<out BbsEntryPoint?>?,
                    value: BbsEntryPoint?,
                    index: Int,
                    isSelected: Boolean,
                    cellHasFocus: Boolean
                ): Component? {
                    rowComponent.apply {
                        background = if (isSelected) list?.selectionBackground else list?.background
                        if (isEnabled != list?.isEnabled) {
                            UIUtil.setEnabled(this, list?.isEnabled ?: false, true)
                        }
                    }

                    aliasComponent.text = value?.alias
                    titleComponent.text = value?.title

                    return rowComponent
                }

                private fun createLabelComponent() = TrimmedMiddleLabel().apply {
                    isOpaque = false
                    border = null
                }
            }
        }
    }
}