# PolyBot

A Discord bot for the [Discord Server](https://discord.dfsek.com) centered around the FOSS Organization,
[Polyhedral Development](https://github.com/PolyhedralDev).

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