package com.github.seclerp.bbsplugin.run

import com.intellij.execution.configurations.RunConfigurationOptions

class BbsRunConfigurationOptions : RunConfigurationOptions() {
    var profile by string(".")
    var entryPoint by string("")

    var additionalArguments by string("")
}