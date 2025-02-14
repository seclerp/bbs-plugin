package com.github.seclerp.bbsplugin.run.components

import com.github.seclerp.bbsplugin.configuration.BbsEntryPoint
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionField
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionRenderer
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionRenderer.Cell
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionRenderer.Companion.append
import com.intellij.openapi.externalSystem.service.ui.completion.collector.TextCompletionCollector
import com.intellij.openapi.observable.properties.ObservableProperty
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleTextAttributes

class BbsEntryPointEditor(
    project: Project?,
    private val entryPoints: ObservableProperty<List<BbsEntryPoint>>
) : TextCompletionField<BbsEntryPoint>(project) {
    override val completionCollector = TextCompletionCollector.basic { entryPoints.get() }

    init {
        renderer = Renderer()
    }

    private class Renderer : TextCompletionRenderer<BbsEntryPoint> {
        override fun getText(item: BbsEntryPoint) = item.alias

        override fun customizeCellRenderer(
            editor: TextCompletionField<BbsEntryPoint>,
            cell: Cell<BbsEntryPoint>
        ) {
            val item = cell.item
            val list = cell.list
            with(cell.component) {
                append(getText(item), editor.text)
                append("  ")
                append(item.description,
                    SimpleTextAttributes.GRAYED_ATTRIBUTES)
            }
        }

        private fun toSingleLineString(text: String) = text
            .replace("\r\n", " ")
            .replace("\n", " ")
    }
}
