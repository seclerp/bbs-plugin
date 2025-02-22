package com.github.seclerp.bbsplugin

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
private class ScopeHolder(val scope: CoroutineScope)

internal val Project.scope: CoroutineScope get() =
    this.service<ScopeHolder>().scope
