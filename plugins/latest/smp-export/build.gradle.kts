import ottermc.Compiler
import ottermc.Constants

plugins {
    kotlin("jvm") version "2.0.0"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/smp-export.jar")
        Compiler.compile(client, Constants.VERSION_LATEST)
    }
    group = "plugins"
}

dependencies {
    implementation(project(":client-latest"))
}