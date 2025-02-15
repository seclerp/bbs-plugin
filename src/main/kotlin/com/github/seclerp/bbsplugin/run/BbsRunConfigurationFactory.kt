package com.github.seclerp.bbsplugin.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.project.Project

class BbsRunConfigurationFactory(private val type: BbsRunConfigurationType) : ConfigurationFactory(type) {
    override fun getId() = type.id

    override fun createTemplateConfiguration(project: Project) = BbsRunConfiguration(project, this, "BBS: Build")
}