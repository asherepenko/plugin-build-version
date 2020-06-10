package com.sherepenko.gradle.plugin.version

import com.sherepenko.gradle.plugin.version.data.BuildVersion
import com.sherepenko.gradle.plugin.version.tasks.Increment
import com.sherepenko.gradle.plugin.version.tasks.IncrementVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke

class BuildVersionPlugin : Plugin<Project> {

    companion object {
        private const val TASK_GROUP = "Versioning"
    }

    private lateinit var extension: BuildVersionExtension

    override fun apply(project: Project) {
        extension = project.extensions.create(
            "buildVersion",
            BuildVersion(project.rootProject.file("version"))
        )

        project.run {
            version = extension.version

            tasks {
                create("incrementMajor", IncrementVersionTask::class) {
                    group = TASK_GROUP
                    description = "Increments the MAJOR and set the MINOR and PATCH versions to 0"
                    increment = Increment.MAJOR
                    version = extension.version
                }

                create("incrementMinor", IncrementVersionTask::class) {
                    group = TASK_GROUP
                    description = "Increments the MINOR and set the PATCH versions to 0"
                    increment = Increment.MINOR
                    version = extension.version
                }

                create("incrementPatch", IncrementVersionTask::class) {
                    group = TASK_GROUP
                    description = "Increments PATCH version"
                    increment = Increment.PATCH
                    version = extension.version
                }

                create("prepareReleaseVersion", IncrementVersionTask::class) {
                    group = TASK_GROUP
                    description = "Prepares release version by stripping pre-release and build metadata"
                    prodRelease = true
                    version = extension.version
                }
            }
        }
    }
}
