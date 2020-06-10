package com.sherepenko.gradle.plugin.version

import com.sherepenko.gradle.plugin.version.data.BuildVersion
import org.gradle.api.tasks.Input

open class BuildVersionExtension(@get:Input var version: BuildVersion) {
    val versionCode: Int
        get() = version.versionCode

    val versionName: String
        get() = version.versionName
}
