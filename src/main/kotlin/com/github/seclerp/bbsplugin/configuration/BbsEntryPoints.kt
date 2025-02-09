package com.github.seclerp.bbsplugin.configuration

// TODO: Extract that information from Bbs (Bbs.Core.EntryPoints.KnownEntryPoints) into JSON file and reuse it here.
object BbsEntryPoints {
    val pwc = BbsEntryPoint(
        title = "PrepareWorkingCopy",
        alias = "pwc",
        description = """
            The first step after each update from the source control.
            Emits solution and project files and prepares everything.
        """.trimIndent(),
        windowsOnly = false
    )

    val build = BbsEntryPoint(
        title = "Build",
        alias = "build",
        description = "Alias for ExtractToBinDir",
        windowsOnly = false
    )

    val extract = BbsEntryPoint(
        title = "ExtractToBinDir",
        alias = "extract",
        description = "",
        windowsOnly = false
    )

    val localInstall = BbsEntryPoint(
        title = "LocalInstall",
        alias = "install",
        description = "Install the product locally.\nPlease select VS version and its hive.",
        windowsOnly = true
    )

    val installer = BbsEntryPoint(
        title = "Installer",
        alias = "installer",
        description = "",
        windowsOnly = true
    )

    val autoFix = BbsEntryPoint(
        title = "AutoFix",
        alias = "autofix",
        description = "",
        windowsOnly = false
    )

    val validation = BbsEntryPoint(
        title = "Validation",
        alias = "validation",
        description = "",
        windowsOnly = false
    )

    val buildNupkgs = BbsEntryPoint(
        title = "Build Nupkgs",
        alias = "nupkgs",
        description = "",
        windowsOnly = false
    )

    val customEntry = BbsEntryPoint(
        title = "Custom Entry",
        alias = "custom",
        description = """
            Runs a build for a custom entry point.
            Use a local or full name of the entry point artifact.
        """.trimIndent(),
        windowsOnly = false
    )

    val binaryStage = BbsEntryPoint(
        title = "Custom Binary",
        alias = "binary",
        description = """
            Runs a build for the given objective on the binary stage, just like on the build server.
            Use a local or full name of the artifact which should be built on the binary stage.
        """.trimIndent(),
        windowsOnly = false
    )

    val sourceStage = BbsEntryPoint(
        title = "Custom Source",
        alias = "source",
        description = """
            Runs a build for the given objective on the source stage.
            The difference from running the custom entry point is that an entry point executes just on Platform Core,
              and the source stage would run on the full build script from all assemblies.
            Use a local or full name of the artifact which should be built on the binary stage.
        """.trimIndent(),
        windowsOnly = false
    )

    val tests = BbsEntryPoint(
        title = "Custom Tests",
        alias = "tests",
        description = """
            Runs the Unit Tests the same way as on TeamCity.
            This means running a build for UnitTestResult binary stage objective
              with the given filter string for the UnitTestRunnerParameters artifact.
        """.trimIndent(),
        windowsOnly = false
    )

    val allEntryPoints = listOf(
        pwc, build, extract, localInstall, installer, autoFix, validation,
        buildNupkgs, customEntry, binaryStage, sourceStage, tests
    )

    val availableEntryPoints: List<BbsEntryPoint> = allEntryPoints.filter { !it.windowsOnly }
    val initialEntryPoints: List<BbsEntryPoint> = listOf(pwc)

    fun resolve(name: String): BbsEntryPoint? {
        val exactMatch = allEntryPoints.firstOrNull { it.alias.equals(name, ignoreCase = true) }
        if (exactMatch != null) return exactMatch

        val matchedEntryPoints = allEntryPoints.filter { it.alias.startsWith(name, ignoreCase = true) }
        return if (matchedEntryPoints.size == 1) matchedEntryPoints.first() else null
    }
}
