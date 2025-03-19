import ottermc.Compiler
import ottermc.Constants

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

dependencies {
    implementation(project(":client-v1.8.9"))
}
