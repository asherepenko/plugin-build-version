plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    kotlin("jvm") version "1.3.72"
    id("com.gradle.plugin-publish") version "0.12.0"
}

group = "com.sherepenko.gradle"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

pluginBundle {
    website = "https://asherepenko.github.io/build-version-plugin/"
    vcsUrl = "https://github.com/asherepenko/build-version-plugin"
    tags = listOf("semantic versioning", "software build version")
}

gradlePlugin {
    plugins {
        create("buildVersionPlugin") {
            id = "com.sherepenko.gradle.version"
            displayName = "Semantic Versioning Plugin"
            description = "This Gradle plugin provides Semantic Versioning 2.0.0 implementation"
            implementationClass = "com.sherepenko.gradle.version.BuildVersionPlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
