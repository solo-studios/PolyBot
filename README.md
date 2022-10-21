# PolyBot

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Pure Kotlin](https://img.shields.io/badge/100%25-kotlin-blue.svg?style=for-the-badge)](https://kotlinlang.org/)
[![Discord Server](https://img.shields.io/discord/871114669761372221?color=7389D8&label=Discord&logo=discord&logoColor=8fa3ff&style=for-the-badge)](https://discord.solo-studios.ca)

PolyBot is a multipurpose discord bot framework written in Kotlin.
It is designed to be highly extensible and easy to re-use features through the plugin system.

PolyBot is currently being rewritten to add support for a powerful plugin system. Once finished, it will be extremely extensible and powerful. Current ETA is unknown.

## Building

To build PolyBot from the source code, simply invoke

```bash
./gradlew build
```

for Linux/MacOS, or

```cmd
gradlew.bat build
```

for Windows, and it will produce a jar file in the `build/libs/` folder. The `*-all.jar` file is a shaded jar,
containing all the dependencies, and the `*.jar` file contains none of them. If you want to download a tarball with all
the required jar files, check the `build/distributions/` folder.