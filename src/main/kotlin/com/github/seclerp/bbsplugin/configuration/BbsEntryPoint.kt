package com.github.seclerp.bbsplugin.configuration

data class BbsEntryPoint(
    val title: String,
    val alias: String,
    val description: String,
    val windowsOnly: Boolean
)