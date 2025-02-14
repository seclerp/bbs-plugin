package com.github.seclerp.bbsplugin.run

import com.github.seclerp.bbsplugin.BbsPaths
import com.github.seclerp.bbsplugin.BbsScriptUtils
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.configurations.WithoutOwnBeforeRunSteps
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import kotlin.jvm.java

class BbsRunConfiguration(
    private val project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<BbsRunConfigurationOptions>(project, factory, name), WithoutOwnBeforeRunSteps {

    var profile: String?
        get() = options.profile
        set(value) { options.profile = value }

    var entryPoint: String?
        get() = options.entryPoint
        set(value) { options.entryPoint = value }

    var additionalArguments: String?
        get() = options.additionalArguments
        set(value) { options.additionalArguments = value }

    override fun getOptions() = super.getOptions() as BbsRunConfigurationOptions

    override fun getOptionsClass(): Class<out RunConfigurationOptions?> = BbsRunConfigurationOptions::class.java

    override fun checkConfiguration() {
        if (!BbsScriptUtils.scriptExists(project))
            throw RuntimeConfigurationError("BBS script can't be found at '${BbsPaths.bbsCmd}'. Is it a monorepo project/solution? ")

        if (profile.isNullOrBlank())
            throw RuntimeConfigurationError("Profile should not be empty. Use `.` as a default profile placeholder.")

        if (entryPoint.isNullOrBlank())
            throw RuntimeConfigurationError("Entry point should not be empty.")
    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment) =
        BbsRunProfileState(this, executionEnvironment)

    override fun getConfigurationEditor() = BbsRunConfigurationEditor(project)
}