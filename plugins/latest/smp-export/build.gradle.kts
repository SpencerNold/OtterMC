import ottermc.Compiler
import ottermc.Constants
import ottermc.VersionController

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/smp-export.jar")
        val file = Compiler.compile(client, Constants.VERSION_LATEST)
        VersionController.handle(file, 65)
    }
    group = "plugins"
}

dependencies {
    implementation(project(":client-latest"))
}
