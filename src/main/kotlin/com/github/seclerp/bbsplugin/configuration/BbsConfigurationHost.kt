package com.github.seclerp.bbsplugin.configuration

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class BbsConfigurationHost {
    companion object {
        fun getInstance(project: Project) = project.service<BbsConfigurationHost>()
    }

    val selectedProfilesPerProject = AtomicProperty(mapOf<String, String>())
    val profiles = AtomicProperty(listOf<String>())
    val entryPoints = AtomicProperty(BbsEntryPoints.allEntryPoints.toList())
}