[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Donate](https://img.shields.io/badge/donate-PayPal-brightgreen.svg)](https://paypal.me/aaronjyoder)

# Riskrieg | Core
Riskrieg is a world-domination/simulation game that lets you play out your fantasy scenarios on a game map. The standard mode, Conquest, plays similarly to traditional world-domination games, while Creative mode allows more D&D-style play.

This is the core Riskrieg game that can be used as a library API in other programs. Game maps are also submitted to this repository.

## Code Style

Please use the Google Java code style when contributing to this repository. You can find the proper file for your IDE [here](https://github.com/google/styleguide).


## Download

Latest release: https://github.com/Riskrieg/riskrieg-core/releases/latest

Make sure you replace **VERSION** with one of the versions from the releases.

**Maven**
```xml
<dependency>
    <groupId>com.riskrieg</groupId>
    <artifactId>riskrieg-core</artifactId>
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
    implementation('com.riskrieg:riskrieg-core:VERSION')
}

repositories {
    mavenCentral() // for transitive dependencies
    maven { url 'https://jitpack.io' }
}
```
