import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    kotlin("jvm") version "1.3.72"
}

group = "com.sherepenko.gradle"
description = "Semantic Versioning Plugin"
version = "0.1.6"

repositories {
    jcenter()
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
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("junit:junit:4.13")
}
