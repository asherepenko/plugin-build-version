package com.sherepenko.gradle.version

import com.sherepenko.gradle.version.data.BuildVersion
import com.sherepenko.gradle.version.tasks.Increment
import com.sherepenko.gradle.version.tasks.IncrementVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class BuildVersionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val buildVersion = BuildVersion(target.rootProject.file("version"))

        target.run {
            tasks {
                register("incrementMajor", IncrementVersionTask::class) {
                    increment = Increment.MAJOR
                    version = buildVersion
                }

                register("incrementMinor", IncrementVersionTask::class) {
                    increment = Increment.MINOR
                    version = buildVersion
                }

                register("incrementPatch", IncrementVersionTask::class) {
                    increment = Increment.PATCH
                    version = buildVersion
                }
            }
        }
    }
}
