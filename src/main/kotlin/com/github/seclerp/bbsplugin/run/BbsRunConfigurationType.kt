package com.github.seclerp.bbsplugin.run

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.icons.AllIcons

class BbsRunConfigurationType : ConfigurationTypeBase(
    "BBS_RUN_CONFIGURATION",
    "BBS Script",
    "Runs bbs.cmd script with specified parameters, if applicable.",
    AllIcons.RunConfigurations.Application
) {

    init {
        addFactory(BbsRunConfigurationFactory(this))
    }
}

