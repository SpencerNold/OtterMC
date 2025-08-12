// Export plugin, meant for updating plugins at runtime (future only!)

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ottermc.Compiler
import ottermc.Constants
import ottermc.BuildTool
import ottermc.KotlinPackageTask

plugins {
    kotlin("jvm") version "1.9.20"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/pvp-export.jar")
        Compiler.compile(client, Constants.VERSION_1_8_9)
    }
    group = "plugins"
}

dependencies {
    implementation(project(":client-v1.8.9"))
}
