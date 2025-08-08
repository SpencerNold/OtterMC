import ottermc.Compiler
import ottermc.Constants

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/smp.jar")
        Compiler.compile(client, Constants.VERSION_LATEST)
    }
    group = "plugins"
}

dependencies {
    implementation(project(":client-latest"))
}
