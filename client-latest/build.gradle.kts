import ottermc.Compiler
import ottermc.Constants
import ottermc.Joiner
import ottermc.RunClientTask

plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

val universal: Configuration by configurations.creating

dependencies {
    // Game Dependencies
    api("org.lwjgl:lwjgl-glfw:3.3.3")
    api("it.unimi.dsi:fastutil:8.5.15")
    api("com.google.code.gson:gson:2.11.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.1")

    // Client dependencies
    universal(project(":universal"))
    for (depend in universal.dependencies)
        api(depend)
    api(files("libs/mc-clean.jar"))

}

tasks.register("attach") {
    doLast {
        val client = file("build/libs/client-latest-remapped-joined.jar")
        RunClientTask.attach(client, Constants.VERSION_LATEST)
    }
    group = "client"
    description = "Attaches the client to a running game client."
    dependsOn("build")
}

tasks.register("run") {
    doLast {
        val client = file("build/libs/client-latest-remapped-joined.jar")
        RunClientTask.launch(client, Constants.VERSION_LATEST)
    }
    group = "client"
    description = "Runs the modified game client."
    dependsOn(":install")
}

tasks.named("build") {
    group = "client"
    doLast {
        val client = file("build/libs/client-latest.jar")
        val mapped = Compiler.compile(client, Constants.VERSION_LATEST)
        Joiner.joinJars(mapped, universal.asPath.split(File.pathSeparator))
    }
}