plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"
    kotlin("jvm") version "1.3.72"
}

group = "com.sherepenko.gradle.plugin"
version = "0.1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

pluginBundle {
    website = "https://asherepenko.github.io/plugin-build-version/"
    vcsUrl = "https://github.com/asherepenko/plugin-build-version"
    tags = listOf("semantic-versioning", "version", "versioning", "auto-increment")
}

gradlePlugin {
    plugins {
        create("buildVersionPlugin") {
            id = "com.sherepenko.gradle.plugin.build-version"
            displayName = "Semantic Versioning Plugin"
            description = "This Gradle plugin provides Semantic Versioning 2.0.0 implementation with auto-increment features"
            implementationClass = "com.sherepenko.gradle.plugin.BuildVersionPlugin"
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.sherepenko.gradle.plugin"
            artifactId = "build-version"
            from(components["java"])
        }
    }
}
