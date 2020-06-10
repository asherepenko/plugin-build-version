plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"
    kotlin("jvm") version "1.3.72"
}

group = "com.sherepenko.gradle"
version = "0.1.0"

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
            description = "Gradle plugin that provides Semantic Versioning 2.0 implementation with auto-increment features"
            implementationClass = "com.sherepenko.gradle.plugin.version.BuildVersionPlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
