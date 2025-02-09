package com.github.seclerp.bbsplugin.settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BbsUserSettingsFile(
    @SerialName("SelectedProfile")
    val selectedProfile: Map<String, String>?
)