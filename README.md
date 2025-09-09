# OtterMC
 OtterMC is a modded Minecraft client that currently is written with SMP servers and Hypixel in mind. These modifications are written with the hope of not giving an unfair advantage, and are allowed on most Minecraft servers. Do not use this client on any server where the modifications are not allowed. 

# Getting Started
Before you can do anything, a couple libraries are required to be able to run the project that aren't able to be packaged with gradle and can't legally be in this repository.

### Installing tools.jar
Go to the maven repository for tools.jar [here](https://mvnrepository.com/artifact/jdk.tools/jdk.tools/1.8.0), and download the jar. Place the jar in `universal/libs/tools.jar`.

### Building mc-clean.jar
OtterMC owns no mappings, so remapped versions of the game are required as dependencies. 

1) For Java 1.8.9, download [Mod Coder Pack](http://www.modcoderpack.com/) and build. You really have two options, you can download and build MCP 1.8.8 and just use that jar, 99.9% of the time that will work fine as the versions are so similar. The other option is to modify the configs so that MCP builds 1.8.9 instead. If you have any questions on how to do this, reach out to me.
2) Place the built 1.8.9 (or 1.8.8) Minecraft jar in `client-v1.8.9/libs/mc-clean.jar`
3) For the latest version of Java Minecraft, the Fabric mappings are used. Download [yarn](https://github.com/FabricMC/yarn) and remap the Minecraft jar with the mappings.
4) Place the built latest version Minecraft jar (currently 1.21.8 is the latest supported) in `client-latest/libs/mc-clean.jar`

NOTE: If running on a newer MacOS computer, make sure the `/usr/libexec/java_home -v 1.8` command can find a jvm made for x86_64 rather than arm64 as older lwjgl versions do not support ARM dylibs.

# Running clients
Once you've modified the code of the clients themselves, you can run a test instance of them with `./gradlew client-<version>:run` on MacOS or `gradlew.bat client-<version>:run` on Windows, where `<version>` is either `latest` or `v1.8.9`.

This will automatically build and install the clients into your Minecraft directory. The game instance running will NOT be of your account, but in fact just of a test version of the account.

# Building
Running `./gradlew build` on MacOS or `gradlew.bat build` on Windows will build the client jars, wrapper jar, and plugin jars for use however you see fit.

Running `./gradlew package` on MacOS or `gradlew.bat package` on Windows will build all of the jars required by the program and bundle them into an installer which will properly install the client jars into the Minecraft directory.

NOTE: `gradlew.bat package` requires [Inno Setup](https://jrsoftware.org/isdl.php) in order to run properly as iscc was used for the installer. Similarly, on MacOS, to run `./gradlew package` requires the `pkgbuild` command to be available at the user level.