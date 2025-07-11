package com.github.seclerp.bbsplugin.execution

object BbsAdditionalCliArguments {
    val help = BbsCliArgument(
        value = "--help",
        description = "Show help"
    )

    // VS and installation options
    val vs = BbsCliArgument(
        value = "--vs",
        description = "VS (LocalInstallTargetArtifact) [=<VALUE>] - Available: latest, latest-stable, 17.0_fa7ec0f0"
    )

    // Compilation options
    val compile = BbsCliArgument(
        value = "--compile",
        description = "Compile All [=false|true] - If disabled, assumes everything ready on disk"
    )
    val compileNative = BbsCliArgument(
        value = "--compile-native",
        description = "Compile Native [=false|true] - If disabled, assumes native code ready on disk"
    )
    val validate = BbsCliArgument(
        value = "--validate",
        description = "Validate Sources [=false|true] - Skip source-stage validation"
    )
    val obfuscate = BbsCliArgument(
        value = "--obfuscate",
        description = "Obfuscate [=false|true] - If disabled, uses original assemblies"
    )
    val osIntegration = BbsCliArgument(
        value = "--os-integration",
        description = "OS Integration [=false|true] - Skip OS integration for standalone hosts"
    )
    val incremental = BbsCliArgument(
        value = "--incremental",
        description = "Incremental Build [=false|true] - Don't turn off for binary subplatforms"
    )

    // Build mode options
    val optimize = BbsCliArgument(
        value = "--optimize",
        description = "Optimize Mode [=false|true] - Turns on compiler optimizations"
    )
    val assert = BbsCliArgument(
        value = "--assert",
        description = "Assert Mode [=false|true] - Controls conditional compilation for assertions"
    )
    val warningsAsErrors = BbsCliArgument(
        value = "--warnings-as-errors",
        description = "Treat Warnings as Errors [=false|true]"
    )
    val reportExceptions = BbsCliArgument(
        value = "--report-exceptions",
        description = "Report Exceptions Mode [=false|true] - Controls extensive exception details"
    )

    // C++ options
    val cppSuppressUnity = BbsCliArgument(
        value = "--cpp-suppress-unity",
        description = "Cpp/CLI Suppress Unity Build [=false|true] - Forces classical build"
    )
    val cppCompileAmd64 = BbsCliArgument(
        value = "--cpp-compile-amd64",
        description = "Cpp/CLI Compile for AMD64 [=false|true]"
    )
    val cppDevMode = BbsCliArgument(
        value = "--cpp-dev-mode",
        description = "Profiler C++ Developer Mode [=false|true]"
    )
    val cppCompilationCache = BbsCliArgument(
        value = "--cpp-compilation-cache",
        description = "Profiler C++ Compilation Cache [=false|true]"
    )

    // Directory and environment options
    val bindir = BbsCliArgument(
        value = "--bindir",
        description = "BinDir [=<VALUE>] - Product binaries directory"
    )
    val root = BbsCliArgument(
        value = "--root",
        description = "RootSuffix [=<VALUE>] - Local install root suffix"
    )
    val env = BbsCliArgument(
        value = "--env",
        description = "Environment [=<VALUE>] - Comma-separated RuntimeID list (e.g., windows-x64,linux-x64)"
    )

    val all = listOf(
        help,
        vs,
        compile,
        compileNative,
        validate,
        obfuscate,
        osIntegration,
        incremental,
        optimize,
        assert,
        warningsAsErrors,
        reportExceptions,
        cppSuppressUnity,
        cppCompileAmd64,
        cppDevMode,
        cppCompilationCache,
        bindir,
        root,
        env
    )
}