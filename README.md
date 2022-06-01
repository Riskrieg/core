<img src="https://user-images.githubusercontent.com/45483768/143955773-61ec00b4-47ca-4973-a013-35aaaf7f1f65.png" align="left"/>

# Riskrieg | Core

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Donate](https://img.shields.io/badge/donate-PayPal-brightgreen.svg)](https://paypal.me/aaronjyoder)

Riskrieg is a world-domination/simulation game that lets you play out scenarios on one of a wide
variety of game maps. The standard mode, Conquest, plays similarly to traditional
world-domination games.

This is the core Riskrieg game that can be used as a library API in other programs. Game maps are to
be submitted to the [maps repository](https://github.com/Riskrieg/maps).
***
## Download

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

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

**Gradle**

```gradle
dependencies {
    implementation('com.riskrieg:core:VERSION')
}

repositories {
    mavenCentral() // for transitive dependencies
    maven { url 'https://jitpack.io' }
}
```
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

***

## Versioning

Prior to June 1, 2022, Riskrieg used semantic versioning, but didn't always follow best practice. Major version changes are always breaking, but minor version bumps were sometimes breaking, when they shouldn't be according to semantic versioning. Patch versions were usually never breaking.

In an attempt to add some clarity to the project version structure, Riskrieg, as of June 1, 2022, adopts a new versioning structure that still works fairly well for versions prior to June 1, 2022.

The new version schema is as follows: **BREAKING.MINOR.NONBREAKING-DATE.PATCH-TAG**

* **BREAKING**: *This version includes major breaking changes compared to the previous BREAKING version.*
* **MINOR**: *This version includes minor breaking changes compared to the previous MINOR version on the same BREAKING version.*
* **NONBREAKING**: *This version includes no breaking changes compared to the previous NONBREAKING version on the same BREAKING.MINOR version. It may include deprecations.*
* **DATE**: *This is a date signifier that uses the following [CalVer](https://calver.org/) format:* **0Y0M**
* **PATCH**: *This version includes no breaking changes or deprecations compared to the previous patch on the same BREAKING.MINOR.NONBREAKING version. It starts at 0, increments as long as DATE remains the same, and resets every time DATE is updated.*
* **TAG**: *This signifies something special about this version, usually that is it pre-release. Valid tags: **dev**, **alpha**, **beta**, **rc***

### Examples
**Version**: 3.0.0-2205.0

**Description**: Contains major breaking changes compared to version 2 and was released in May of 2022.

**Version**: 3.1.2-2206.3

**Description**: Contains major breaking changes compared to version 2, contains minor breaking changes compared to 3.0.x, contains no breaking changes compared to 3.1.x, was released in June of 2022, and has been updated three times so far in June.


**Version**: 3.1.7-2206.14-alpha

**Description**: Contains major breaking changes compared to version 2, contains minor breaking changes compared to 3.0.x, contains no breaking changes compared to 3.1.x, was released in June of 2022, and has been updated fourteen times so far in June. This is an alpha release.

***

## Contributing

Please [join us on Discord](https://discord.gg/weU8jYDbW4) if you'd like to be more involved in
contributing to Riskrieg.

### Code Style

Please use the Google Java code style when contributing to this repository. You can find the proper
file for your IDE [here](https://github.com/google/styleguide).
