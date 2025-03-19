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
        Compiler.compile(client, Constants.VERSION_1_21_4)
    }
    group = "plugins"
}

dependencies {
    implementation(project(":client-v1.21.4"))
}
