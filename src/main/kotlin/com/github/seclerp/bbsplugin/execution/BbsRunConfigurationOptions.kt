package com.github.seclerp.bbsplugin.execution

import com.intellij.execution.configurations.RunConfigurationOptions

class BbsRunConfigurationOptions : RunConfigurationOptions() {
    var profile by string(".")
    var entryPoint by string("")

    var additionalArguments by string("")
}