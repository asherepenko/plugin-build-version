package com.sherepenko.gradle.plugin.version

import java.io.File

class BuildVersion(private val versionFile: File) {
    companion object {
        private val VERSION_PATTERN = Regex(
            """
            (0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?
            (?:-([\dA-z\-]+(?:\.[\dA-z\-]+)*))?
            (?:\+([\dA-z\-]+(?:\.[\dA-z\-]+)*))?
            """.trimIndent()
        )

        private val PRE_RELEASE_PATTERN = Regex(
            """[\dA-z\-]+(?:\.[\dA-z\-]+)*"""
        )

        private val BUILD_METADATA_PATTERN = Regex(
            """[\dA-z\-]+(?:\.[\dA-z\-]+)*"""
        )

        private fun parseBuildVersion(file: File): MatchResult {
            if (file.exists() && file.canRead()) {
                val versionText = file.readText()
                return VERSION_PATTERN.matchEntire(versionText)
                    ?: throw IllegalArgumentException(
                        "Unable to parse build version: $versionText"
                    )
            } else {
                throw IllegalArgumentException(
                    "Unable to read version file: ${file.path}"
                )
            }
        }
    }

    // 2 digits
    internal var major: Int
        private set

    // 3 digits
    internal var minor: Int
        private set

    // 2 digits
    internal var patch: Int
        private set

    internal var preRelease: String?
        private set

    internal var buildMetadata: String?
        private set

    init {
        val result = parseBuildVersion(versionFile)

        major = result.groupValues[1].toInt()
        minor = result.groupValues[2].toInt()
        patch = result.groupValues[3].toInt()
        preRelease = if (result.groupValues[4].isNotEmpty()) {
            result.groupValues[4]
        } else {
            null
        }
        buildMetadata = if (result.groupValues[5].isNotEmpty()) {
            result.groupValues[5]
        } else {
            null
        }

        require(major >= 0) { "Major version must be a positive number" }
        require(minor >= 0) { "Minor version must be a positive number" }
        require(patch >= 0) { "Patch version must be a positive number" }
        require(major > 0 || minor > 0 || patch > 0) {
            "At least one version number must be greater than 0"
        }

        preRelease?.let {
            require(it.matches(PRE_RELEASE_PATTERN)) {
                "Pre-release version is not valid"
            }
        }

        buildMetadata?.let {
            require(it.matches(BUILD_METADATA_PATTERN)) {
                "Build metadata is not valid"
            }
        }
    }

    val versionCode: Int
        get() = major * 10000000 + minor * 10000 + patch * 100

    val versionName: String
        get() = buildString {
            append("$major.$minor.$patch")

            preRelease?.let {
                append("-$it")
            }

            buildMetadata?.let {
                append("+$it")
            }
        }

    // Trim pre-release and build metadata
    fun prepareProdRelease() {
        preRelease = null
        buildMetadata = null
    }

    fun incrementMajor() {
        major++
        minor = 0
        patch = 0
        ensureVersion()
        versionFile.writeText(versionName)
    }

    fun incrementMinor() {
        minor++
        patch = 0
        ensureVersion()
        versionFile.writeText(versionName)
    }

    fun incrementPatch() {
        patch++
        ensureVersion()
        versionFile.writeText(versionName)
    }

    private fun ensureVersion() {
        if (patch >= 100) {
            minor++
            patch = 0
        }

        if (minor >= 1000) {
            major++
            minor = 0
        }
    }
}
