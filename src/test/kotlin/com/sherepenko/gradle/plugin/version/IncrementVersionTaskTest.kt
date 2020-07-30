package com.sherepenko.gradle.plugin.version

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class IncrementVersionTaskTest {

    private lateinit var buildVersion: BuildVersion

    private lateinit var project: Project

    @Before
    fun setUp() {
        buildVersion = mockk<BuildVersion>().apply {
            every { versionCode } returns 100
            every { versionName } returns "0.0.1"
        }

        project = ProjectBuilder.builder().build()
    }

    @Test
    fun `should call prepare RELEASE version`() {
        val task = project.tasks.create(
            "prepareReleaseVersion",
            IncrementVersionTask::class
        ).apply {
            prodRelease = true
            version = buildVersion
        }

        task.increment()

        verify { buildVersion.prepareProdRelease() }
    }

    @Test
    fun `should call increment PATCH version`() {
        val task = project.tasks.create("incrementPatch", IncrementVersionTask::class).apply {
            increment = Increment.PATCH
            version = buildVersion
        }

        task.increment()

        verify { buildVersion.incrementPatch() }
    }

    @Test
    fun `should call increment MINOR version`() {
        val task = project.tasks.create("incrementMinor", IncrementVersionTask::class).apply {
            increment = Increment.MINOR
            version = buildVersion
        }

        task.increment()

        verify { buildVersion.incrementMinor() }
    }

    @Test
    fun `should call increment MAJOR version`() {
        val task = project.tasks.create("incrementMajor", IncrementVersionTask::class).apply {
            increment = Increment.MAJOR
            version = buildVersion
        }

        task.increment()

        verify { buildVersion.incrementMajor() }
    }
}
