<img src="https://user-images.githubusercontent.com/45483768/143955773-61ec00b4-47ca-4973-a013-35aaaf7f1f65.png" align="left"/>

# Riskrieg | Core

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-white.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.riskrieg/core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.riskrieg%22%20AND%20a:%22core%22)
[![Donate](https://img.shields.io/badge/Donate-PayPal-lightgreen.svg)](https://paypal.me/aaronjyoder)

Riskrieg is a world-domination/simulation game that lets you play out scenarios on one of a wide
variety of game maps. The standard mode, Conquest, plays similarly to traditional
world-domination games.

This is the core Riskrieg game that can be used as a library API in other programs. Game maps are to
be submitted to the [maps repository](https://github.com/Riskrieg/maps).
***
## Download
[![Maven Central](https://img.shields.io/maven-central/v/com.riskrieg/core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.riskrieg%22%20AND%20a:%22core%22)

Latest release: https://github.com/Riskrieg/core/releases/latest

Make sure you replace **VERSION** with one of the versions from the releases.

**Maven**

```xml
<dependency>
    <groupId>com.riskrieg</groupId>
    <artifactId>core</artifactId>
    <version>VERSION</version>
</dependency>
```

**Gradle**

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation('com.riskrieg:core:VERSION')
}
```

Starting with version `3.0.0-1.2206-alpha`, Riskrieg Core is available from Maven Central.
If you need any version prior to `3.0.0-1.2206-alpha`, you need to declare Jitpack as a repository. You can figure out how to do that [here](https://jitpack.io/).
***

### Usage

Instantiating the API and creating a group.
```java
Riskrieg api = RiskriegBuilder.createLocal(Path.of(BotConstants.REPOSITORY_PATH)).build();

// Create a Group
Group group = api.createGroup(GroupIdentifier.of("your-id")).complete(); // Can throw a Runtime Exception
// OR
// Exceptions wrapped in the failure block
api.createGroup(GroupIdentifier.of("your-id")).queue(group -> { /* do something */ }, throwable -> { /* do something else */ });
```

Retrieving a Group and creating a Game.
```java
Riskrieg api = RiskriegBuilder.createLocal(Path.of(BotConstants.REPOSITORY_PATH)).build();
Group group = api.retrieveGroup(GroupIdentifier.of("your-id")).complete();
Game game = group.createGame(GameIdentifier.of("game-id"), Conquest.class).complete(); // Create a new Conquest game
```

The `.complete()` method does not currently actively block threads, but it should be treated as if it does, because it will eventually.
Likewise, `.queue()` could eventually make non-blocking network requests, so it should be treated as if it does.

***

## Versioning

Prior to June 1, 2022, Riskrieg used semantic versioning, but didn't always follow best practice. Major version changes are always breaking, but minor version bumps were sometimes breaking, when they shouldn't be according to semantic versioning. Patch versions were usually never breaking.

In an attempt to add some clarity to the project version structure, Riskrieg, as of June 1, 2022, adopts a new versioning structure that still works fairly well for versions prior to June 1, 2022.

The new version schema is as follows: **BREAKING.MINOR.NONBREAKING-REVISION.DATE-CLASSIFIER**

* **BREAKING**: *This version includes major breaking changes compared to the previous BREAKING version.*
* **MINOR**: *This version includes minor breaking changes compared to the previous MINOR version on the same BREAKING version.*
* **NONBREAKING**: *This version includes no breaking changes compared to the previous NONBREAKING version on the same BREAKING.MINOR version. It may include deprecations.*
* **REVISION**: *Starts at 0, and 1-9 should be zero-padded. This version includes no breaking changes or deprecations compared to the previous patch on the same BREAKING.MINOR.NONBREAKING version. Increments as long as DATE remains the same, and resets every time DATE or any version before it changes.*
* **DATE**: *This is a date signifier that uses the following [CalVer](https://calver.org/) format:* **0Y0M**
* **CLASSIFIER**: *This signifies something special about this version, usually that it is pre-release. Valid classifiers: **dev**, **alpha**, **beta**, **rc***

### Examples
**Version**: 3.0.0-0.2205

**Description**: Contains major breaking changes compared to version 2 and was released in May of 2022.

**Version**: 3.1.2-3.2206

**Description**: Contains major breaking changes compared to version 2, contains minor breaking changes compared to 3.0.x, contains no breaking changes compared to 3.1.x, was released in June of 2022, and has been updated three times so far in June.


**Version**: 3.1.7-14.2206-alpha

**Description**: Contains major breaking changes compared to version 2, contains minor breaking changes compared to 3.0.x, contains no breaking changes compared to 3.1.x, was released in June of 2022, and has been updated fourteen times so far in June. This is an alpha release.

***

## Contributing

Please [join us on Discord](https://discord.gg/weU8jYDbW4) if you'd like to be more involved in
contributing to Riskrieg.

### Code Style

Please use the Google Java code style when contributing to this repository. You can find the proper
file for your IDE [here](https://github.com/google/styleguide).
