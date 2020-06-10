# Semantic Versioning Plugin

Gradle plugin that provides Semantic Versioning 2.0.0 implementation with auto-increment features.

## Summary

Given a version number `MAJOR.MINOR.PATCH`, increment the:

1. `MAJOR` version when you make incompatible API changes,
2. `MINOR` version when you add functionality in a backward compatible manner, and
3. `PATCH` version when you make backward compatible bug fixes.

Additional labels for pre-release and build metadata are available as extensions to the `MAJOR.MINOR.PATCH` format.

## Gradle Tasks

- `incrementMajor` will increment the `MAJOR` and set the `MINOR` and `PATCH` versions to `0`;
- `incrementMinor` will increment the `MINOR` and set the `PATCH` versions to `0`;
- `incrementPatch` will increment the `PATCH` version;
- `prepareReleaseVersion` will prepare release version by stripping pre-release and build metadata
