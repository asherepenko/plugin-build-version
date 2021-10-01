import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.16.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    kotlin("jvm") version "1.5.31"
}

group = "com.sherepenko.gradle"
description = "Semantic Versioning Plugin"
version = "0.3.0"

repositories {
    mavenCentral()
}

pluginBundle {
    website = "https://asherepenko.github.io/plugin-build-version/"
    vcsUrl = "https://github.com/asherepenko/plugin-build-version"
    tags = listOf("semantic-versioning", "version", "versioning", "auto-increment")

    mavenCoordinates {
        groupId = project.group as String
        artifactId = "plugin-build-version"
    }
}

gradlePlugin {
    plugins {
        create("buildVersionPlugin") {
            id = "com.sherepenko.gradle.plugin-build-version"
            displayName = "Semantic Versioning Plugin"
            description = "Gradle plugin that provides Semantic Versioning 2.0 implementation " +
                "with auto-increment features"
            implementationClass = "com.sherepenko.gradle.plugin.version.BuildVersionPlugin"
        }
    }
}

ktlint {
    verbose.set(true)
    android.set(true)

    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.12.0")
}
