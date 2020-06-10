package com.sherepenko.gradle.plugin.version

import com.google.common.truth.Truth.assertThat
import java.io.File
import java.lang.IllegalArgumentException
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BuildVersionTest {

    @Rule
    @JvmField
    val projectDir = TemporaryFolder()

    @Test
    fun `should fail if version file does not exist`() {
        val error = assertThrows(IllegalArgumentException::class.java) {
            BuildVersion(File("version"))
        }

        assertThat(error).hasMessageThat().startsWith("Unable to read version file")
    }

    @Test
    fun `should fail if unable to parse build version from file`() {
        val versionFile = projectDir.newFile("version").apply {
            writeText("unknown")
        }

        val error = assertThrows(IllegalArgumentException::class.java) {
            BuildVersion(versionFile)
        }

        assertThat(error).hasMessageThat().startsWith("Unable to parse build version")
    }

    @Test
    fun `should successfully parse build version from file`() {
        val major = 1
        val minor = 0
        val patch = 0
        val preRelease = "alpha01"
        val buildMetadata = "meta"

        val versionCode = major * 10000000 + minor * 10000 + patch * 100
        val versionName = "$major.$minor.$patch-$preRelease+$buildMetadata"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.preRelease).isEqualTo(preRelease)
        assertThat(buildVersion.buildMetadata).isEqualTo(buildMetadata)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should prepare RELEASE version`() {
        val major = 1
        val minor = 0
        val patch = 0
        val preRelease = "alpha01"
        val buildMetadata = "meta"

        val versionCode = major * 10000000 + minor * 10000 + patch * 100
        val versionName = "$major.$minor.$patch-$preRelease+$buildMetadata"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.preRelease).isEqualTo(preRelease)
        assertThat(buildVersion.buildMetadata).isEqualTo(buildMetadata)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.prepareProdRelease()

        assertThat(buildVersion.preRelease).isNull()
        assertThat(buildVersion.buildMetadata).isNull()
        assertThat(buildVersion.versionName).isEqualTo("$major.$minor.$patch")
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should increment PATCH version`() {
        val major = 1
        val minor = 1
        val patch = 1

        var versionCode = major * 10000000 + minor * 10000 + patch * 100
        var versionName = "$major.$minor.$patch"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.incrementPatch()

        versionCode = major * 10000000 + minor * 10000 + (patch + 1) * 100
        versionName = "$major.$minor.${patch + 1}"

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch + 1)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should increment MINOR version`() {
        val major = 1
        val minor = 1
        val patch = 1

        var versionCode = major * 10000000 + minor * 10000 + patch * 100
        var versionName = "$major.$minor.$patch"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.incrementMinor()

        versionCode = major * 10000000 + (minor + 1) * 10000
        versionName = "$major.${minor + 1}.0"

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor + 1)
        assertThat(buildVersion.patch).isEqualTo(0)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should increment MAJOR version`() {
        val major = 1
        val minor = 1
        val patch = 1

        var versionCode = major * 10000000 + minor * 10000 + patch * 100
        var versionName = "$major.$minor.$patch"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.incrementMajor()

        versionCode = (major + 1) * 10000000
        versionName = "${major + 1}.0.0"

        assertThat(buildVersion.major).isEqualTo(major + 1)
        assertThat(buildVersion.minor).isEqualTo(0)
        assertThat(buildVersion.patch).isEqualTo(0)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should increment MINOR by incrementing PATCH version if PATCH = 99`() {
        val major = 1
        val minor = 1
        val patch = 99

        var versionCode = major * 10000000 + minor * 10000 + patch * 100
        var versionName = "$major.$minor.$patch"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.incrementPatch()

        versionCode = major * 10000000 + (minor + 1) * 10000
        versionName = "$major.${minor + 1}.0"

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor + 1)
        assertThat(buildVersion.patch).isEqualTo(0)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should increment MAJOR by incrementing MINOR version if MINOR = 999`() {
        val major = 1
        val minor = 999
        val patch = 1

        var versionCode = major * 10000000 + minor * 10000 + patch * 100
        var versionName = "$major.$minor.$patch"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.incrementMinor()

        versionCode = (major + 1) * 10000000
        versionName = "${major + 1}.0.0"

        assertThat(buildVersion.major).isEqualTo(major + 1)
        assertThat(buildVersion.minor).isEqualTo(0)
        assertThat(buildVersion.patch).isEqualTo(0)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }

    @Test
    fun `should increment MAJOR by incrementing PATCH version if PATCH = 99 and MINOR = 999`() {
        val major = 1
        val minor = 999
        val patch = 99

        var versionCode = major * 10000000 + minor * 10000 + patch * 100
        var versionName = "$major.$minor.$patch"

        val versionFile = projectDir.newFile("version").apply {
            writeText(versionName)
        }

        val buildVersion = BuildVersion(versionFile)

        assertThat(buildVersion.major).isEqualTo(major)
        assertThat(buildVersion.minor).isEqualTo(minor)
        assertThat(buildVersion.patch).isEqualTo(patch)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)

        buildVersion.incrementPatch()

        versionCode = (major + 1) * 10000000
        versionName = "${major + 1}.0.0"

        assertThat(buildVersion.major).isEqualTo(major + 1)
        assertThat(buildVersion.minor).isEqualTo(0)
        assertThat(buildVersion.patch).isEqualTo(0)
        assertThat(buildVersion.versionName).isEqualTo(versionName)
        assertThat(buildVersion.versionCode).isEqualTo(versionCode)
    }
}
