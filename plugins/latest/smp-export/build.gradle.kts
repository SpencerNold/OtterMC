import ottermc.Compiler
import ottermc.Constants
import ottermc.VersionController

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
        val file = Compiler.compile(client, Constants.VERSION_LATEST)
        VersionController.handle(file, 65)
    }
    group = "plugins"
}


tasks.jar {
    from({
        configurations.runtimeClasspath.get()
            .filter { dep ->
                dep.name.startsWith("kotlin-stdlib")
            }
            .map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":client-latest"))
}