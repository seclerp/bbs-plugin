package com.github.seclerp.bbsplugin.execution.components

import com.intellij.openapi.externalSystem.service.ui.command.line.CommandLineField
import com.intellij.openapi.externalSystem.service.ui.command.line.CommandLineInfo
import com.intellij.openapi.externalSystem.service.ui.command.line.CompletionTableInfo
import com.intellij.openapi.externalSystem.service.ui.completion.TextCompletionInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.jetbrains.rd.util.lifetime.Lifetime

object BbsAdditionalArgumentsEditor {
    fun create(project: Project, lifetime: Lifetime): CommandLineField {
        return CommandLineField(project, BbsCommandLineInfo(), lifetime.createNestedDisposable())
    }
    private class BbsCommandLineInfo : CommandLineInfo {
        override val dialogTitle = "bbs.cmd"
        override val dialogTooltip = "Show BBS.cmd command line reference"
        override val fieldEmptyState = "Custom BBS.cmd arguments"
        override val tablesInfo: List<CompletionTableInfo> = listOf(
            object : CompletionTableInfo {
                override val emptyState = "No values"
                override val dataColumnIcon = null
                override val dataColumnName = "Argument"
                override val descriptionColumnIcon = null
                override val descriptionColumnName = "Description"

                override suspend fun collectCompletionInfo() =
                    listOf(TextCompletionInfo("--help", "Show help"))

                override suspend fun collectTableCompletionInfo() = collectCompletionInfo()
            }
        )
        override val settingsName = null
        override val settingsHint = null
    }
}