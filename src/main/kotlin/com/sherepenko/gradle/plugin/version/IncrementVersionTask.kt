package com.sherepenko.gradle.plugin.version

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

enum class Increment {
    MAJOR,
    MINOR,
    PATCH,
    NONE,
}

abstract class IncrementVersionTask : DefaultTask() {
    @get:Input
    var prodRelease: Boolean = false

    @get:Input
    var increment: Increment = Increment.NONE

    @get:Input
    abstract var version: BuildVersion

    @TaskAction
    fun increment() {
        println("Incrementing ${increment.name} version...")

        val prevVersionCode = version.versionCode
        val prevVersionName = version.versionName

        if (prodRelease) {
            println("Prepare release version...")
            version.prepareProdRelease()
        }

        when (increment) {
            Increment.MAJOR -> {
                version.incrementMajor()
            }
            Increment.MINOR -> {
                version.incrementMinor()
            }
            Increment.PATCH -> {
                version.incrementPatch()
            }
            else -> {
                // ignore
            }
        }

        println("$prevVersionName ($prevVersionCode) -> ${version.versionName} (${version.versionCode})")
    }
}
