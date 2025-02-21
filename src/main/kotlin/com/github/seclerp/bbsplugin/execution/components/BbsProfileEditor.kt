package com.github.seclerp.bbsplugin.execution.components

import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionField
import com.intellij.openapi.externalSystem.service.ui.completion.collector.TextCompletionCollector
import com.intellij.openapi.observable.properties.ObservableProperty
import com.intellij.openapi.project.Project

class BbsProfileEditor(
    project: Project?,
    private val profiles: ObservableProperty<List<String>>
) : TextCompletionField<String>(project) {
    override val completionCollector = TextCompletionCollector.Companion.basic { getKnownProfileValues() }

    private fun getKnownProfileValues() = buildList {
        add(".")
        addAll(profiles.get())
    }
}