import ottermc.Compiler
import ottermc.Constants
import ottermc.VersionController

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/pvp-export.jar")
        val file = Compiler.compile(client, Constants.VERSION_1_8_9)
        VersionController.handle(file, 52)
    }
    group = "plugins"
}

dependencies {
    implementation(project(":client-v1.8.9"))
}
