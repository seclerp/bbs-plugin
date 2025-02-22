package com.github.seclerp.bbsplugin.actions

import com.github.seclerp.bbsplugin.configuration.BbsConfigurationHost
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.CheckBoxList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.application
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.DefaultListModel
import javax.swing.JPanel

class BbsSelectEntryPointsAction(private val project: Project) : AnAction("Select Entry Points...") {
    private val configHost by lazy { BbsConfigurationHost.getInstance(project) }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        application.invokeLater {
            val entryPoints = configHost.entryPoints.get()
            val alreadySelectedEntryPoints = configHost.selectedEntryPoints.get().toHashSet()
            val entryPointsSelection = entryPoints
                .map { alreadySelectedEntryPoints.contains(it.alias) }
                .toTypedArray()

            val selector = CheckBoxList<String>().apply {
                border = JBUI.Borders.empty()
                entryPoints.forEach { addItem(it.alias, it.alias, alreadySelectedEntryPoints.contains(it.alias)) }
                setCheckBoxListListener { index, checked ->
                    entryPointsSelection[index] = checked
                }
            }

            val panel = JPanel(BorderLayout()).apply {
                add(selector, BorderLayout.CENTER)
            }

            val scrollPane = JBScrollPane(panel).apply {
                minimumSize = Dimension(JBUI.scale(350), JBUI.scale(200))
            }

            val dialog = DialogBuilder(project).apply {
                centerPanel(scrollPane)
            }

            if (dialog.showAndGet()) {
                val selectedEntryPoints = entryPoints.map { it.alias }.filterIndexed { index, _ -> entryPointsSelection[index] }
                configHost.setSelectedEntryPoints(selectedEntryPoints)
            }
        }
    }
}