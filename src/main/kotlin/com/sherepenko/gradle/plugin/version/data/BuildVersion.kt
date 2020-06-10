package com.sherepenko.gradle.plugin.version.data

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

class BuildVersion(private val versionFile: File) {
    companion object {
        private val VERSION_PATTERN = Regex(
            """(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:-([\dA-z\-]+(?:\.[\dA-z\-]+)*))?(?:\+([\dA-z\-]+(?:\.[\dA-z\-]+)*))?"""
        )

        private val PRE_RELEASE_PATTERN = Regex(
            """[\dA-z\-]+(?:\.[\dA-z\-]+)*"""
        )

        private val BUILD_METADATA_PATTERN = Regex(
            """[\dA-z\-]+(?:\.[\dA-z\-]+)*"""
        )

        private const val MAX_CANDIDATE_VALUE = 99
    }

    // 2 digits
    private var major: Int

    // 3 digits
    private var minor: Int

    // 2 digits
    private var patch: Int

    // 2 digits
    private var candidate: Int

    private var preRelease: String?

    private var buildMetadata: String?

    init {
        if (versionFile.exists() && versionFile.canRead()) {
            val versionText = versionFile.readText()
            val result = VERSION_PATTERN.matchEntire(versionText)
                ?: throw IllegalArgumentException("Unable to parse build version: $versionText")

            major = result.groupValues[1].toInt()
            minor = result.groupValues[2].toInt()
            patch = result.groupValues[3].toInt()
            candidate = 0
            preRelease = if (result.groupValues[4].isEmpty()) null else result.groupValues[4]
            buildMetadata = if (result.groupValues[5].isEmpty()) null else result.groupValues[5]
        } else {
            throw IllegalArgumentException("Unable to read version file: ${versionFile.path}")
        }

        require(major >= 0) { "Major version must be a positive number" }
        require(minor >= 0) { "Minor version must be a positive number" }
        require(patch >= 0) { "Patch version must be a positive number" }
        require(major > 0 || minor > 0 || patch > 0) {
            "At least one version number must be greater than 0"
        }

        preRelease?.let {
            require(it.matches(PRE_RELEASE_PATTERN)) { "Pre-release version is not valid" }
            candidate = min(
                max(it.replace(Regex("""[^0-9]"""), "").toIntOrNull() ?: 0, 0),
                MAX_CANDIDATE_VALUE
            )
        }

        buildMetadata?.let {
            require(it.matches(BUILD_METADATA_PATTERN)) { "Build metadata is not valid" }
        }
    }

    val versionCode: Int
        get() = major * 100000000 + minor * 10000 + patch * 100 + candidate

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
        candidate = 0
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
