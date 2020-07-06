# Semantic Versioning Plugin

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![CircleCI](https://circleci.com/gh/asherepenko/plugin-build-version.svg?style=svg&circle-token=384648b6b628cea4af1f4d834f1788ffaafa9f31)](https://circleci.com/gh/asherepenko/plugin-build-version)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/com/sherepenko/gradle/plugin-build-version/com.sherepenko.gradle.plugin-build-version.gradle.plugin/maven-metadata.xml.svg?colorB=007ec6&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/com.sherepenko.gradle.plugin-build-version)

Gradle plugin that provides [Semantic Versioning](https://semver.org/) with auto-increment features.

## How to

Add it in your project `build.gradle.kts` at the end of plugins.

**Using the plugins DSL:**

```kotlin
plugins {
    id("com.sherepenko.gradle.plugin-build-version") version "x.y.z"
}
```

**Using legacy plugin application:**

```kotlin
buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.sherepenko.gradle.plugin-build-version:x.y.z")
    }
}

apply(plugin = "com.sherepenko.gradle.plugin-build-version")
```

## Summary

Given a version number `MAJOR.MINOR.PATCH`, increment the:

1. `MAJOR` version when you make incompatible API changes,
2. `MINOR` version when you add functionality in a backward compatible manner, and
3. `PATCH` version when you make backward compatible bug fixes.

Additional labels for pre-release and build metadata are available as extensions to the `MAJOR.MINOR.PATCH` format.

## Gradle tasks

- `incrementMajor` will increment the `MAJOR` and set the `MINOR` and `PATCH` versions to `0`;
- `incrementMinor` will increment the `MINOR` and set the `PATCH` version to `0`;
- `incrementPatch` will increment the `PATCH` version;
- `prepareReleaseVersion` will prepare release version by trimming pre-release and build metadata
