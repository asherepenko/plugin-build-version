package com.sherepenko.gradle.plugin.version

import com.sherepenko.gradle.plugin.version.BuildVersion.Companion.MAJOR_VERSION_MASK
import com.sherepenko.gradle.plugin.version.BuildVersion.Companion.MINOR_VERSION_MASK
import com.sherepenko.gradle.plugin.version.BuildVersion.Companion.PATCH_VERSION_MASK

fun newVersionCode(major: Int, minor: Int, patch: Int): Int =
    major * MAJOR_VERSION_MASK + minor * MINOR_VERSION_MASK + patch * PATCH_VERSION_MASK

fun newVersionName(
    major: Int,
    minor: Int,
    patch: Int,
    preRelease: String? = null,
    buildMetadata: String? = null
): String = buildString {
    append("$major.$minor.$patch")

    preRelease?.let {
        append("-$it")
    }

    buildMetadata?.let {
        append("+$it")
    }
}
