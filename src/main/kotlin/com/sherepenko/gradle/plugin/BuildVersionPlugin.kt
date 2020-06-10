package com.sherepenko.gradle.plugin

import com.sherepenko.gradle.plugin.data.BuildVersion
import com.sherepenko.gradle.plugin.tasks.Increment
import com.sherepenko.gradle.plugin.tasks.IncrementVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke

class BuildVersionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = project.extensions.create<BuildVersionConfig>(
            "versionConfig",
                BuildVersion(project.rootProject.file("version"))
        )

        project.run {
            tasks {
                create("incrementMajor", IncrementVersionTask::class) {
                    increment = Increment.MAJOR
                    version = config.version
                }

                create("incrementMinor", IncrementVersionTask::class) {
                    increment = Increment.MINOR
                    version = config.version
                }

                create("incrementPatch", IncrementVersionTask::class) {
                    increment = Increment.PATCH
                    version = config.version
                }
            }
        }
    }
}
