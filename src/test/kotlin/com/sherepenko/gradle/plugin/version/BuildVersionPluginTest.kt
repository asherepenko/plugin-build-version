package com.sherepenko.gradle.plugin.version

import com.google.common.truth.Truth.assertThat
import java.io.File
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BuildVersionPluginTest {

    @Rule
    @JvmField
    val projectDir = TemporaryFolder()

    private lateinit var versionFile: File

    private lateinit var buildFile: File

    private lateinit var project: Project

    @Before
    fun setUp() {
        versionFile = projectDir.newFile("version").apply {
            writeText("1.0.0")
        }

        buildFile = projectDir.newFile("build.gradle.kts")

        project = ProjectBuilder.builder()
            .withProjectDir(projectDir.root)
            .build()
    }

    @Test
    fun `should register versioning tasks`() {
        project.pluginManager.apply("com.sherepenko.gradle.plugin-build-version")

        val tasks = project.tasks

        assertThat(tasks).isNotNull()
        assertThat(tasks).hasSize(4)

        assertThat(tasks["prepareReleaseVersion"]).isInstanceOf(IncrementVersionTask::class.java)

        val prepareReleaseVersionTask = tasks["prepareReleaseVersion"] as IncrementVersionTask
        assertThat(prepareReleaseVersionTask.prodRelease).isTrue()
        assertThat(prepareReleaseVersionTask.increment).isEqualTo(Increment.NONE)
        assertThat(prepareReleaseVersionTask.version).isNotNull()

        assertThat(tasks["incrementPatch"]).isInstanceOf(IncrementVersionTask::class.java)

        val incrementPatchTask = tasks["incrementPatch"] as IncrementVersionTask
        assertThat(incrementPatchTask.prodRelease).isFalse()
        assertThat(incrementPatchTask.increment).isEqualTo(Increment.PATCH)
        assertThat(incrementPatchTask.version).isNotNull()

        assertThat(tasks["incrementMinor"]).isInstanceOf(IncrementVersionTask::class.java)

        val incrementMinorTask = tasks["incrementMinor"] as IncrementVersionTask
        assertThat(incrementMinorTask.prodRelease).isFalse()
        assertThat(incrementMinorTask.increment).isEqualTo(Increment.MINOR)
        assertThat(incrementMinorTask.version).isNotNull()

        assertThat(tasks["incrementMajor"]).isInstanceOf(IncrementVersionTask::class.java)

        val incrementMajorTask = tasks["incrementMajor"] as IncrementVersionTask
        assertThat(incrementMajorTask.prodRelease).isFalse()
        assertThat(incrementMajorTask.increment).isEqualTo(Increment.MAJOR)
        assertThat(incrementMajorTask.version).isNotNull()
    }
}
