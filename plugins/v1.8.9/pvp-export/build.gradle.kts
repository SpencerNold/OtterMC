// Export plugin, meant for updating plugins at runtime (future only!)

import ottermc.Compiler
import ottermc.Constants

plugins {
    kotlin("jvm") version "2.0.0"
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

tasks.jar {
    from({
        configurations.runtimeClasspath.get()
            .filter {
                it.name.startsWith("kotlin-stdlib")
            }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":client-v1.8.9"))
}
