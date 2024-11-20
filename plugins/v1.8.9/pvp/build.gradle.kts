import ottermc.Compiler
import ottermc.Constants
import ottermc.Joiner

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/pvp.jar")
        Compiler.compile(client, Constants.VERSION_1_8_9)
    }
    group = "plugins"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":client-v1.8.9"))
}