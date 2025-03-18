import ottermc.Compiler
import ottermc.RunClientTask
import ottermc.Constants
import ottermc.Joiner

plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
}

val universal: Configuration by configurations.creating

dependencies {
    // Game Dependencies
    api("org.lwjgl:lwjgl-glfw:3.3.3")
    api("it.unimi.dsi:fastutil:8.5.15")

    // Client dependencies
    universal(project(":universal"))
    for (depend in universal.dependencies)
        api(depend)
    api(files("libs/mc-clean.jar"))

}

tasks.register("attach") {
    doLast {
        val client = file("build/libs/client-v1.21.4-remapped-joined.jar")
        RunClientTask.attach(client, Constants.VERSION_1_21_4)
    }
    group = "client"
    description = "Attaches the client to a running game client."
    dependsOn("build")
}

tasks.register("run") {
    doLast {
        val client = file("build/libs/client-v1.21.4-remapped-joined.jar")
        RunClientTask.launch(client, Constants.VERSION_1_21_4)
    }
    group = "client"
    description = "Runs the modified game client."
    dependsOn(":install")
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/client-v1.21.4.jar")
        val mapped = Compiler.compile(client, Constants.VERSION_1_21_4)
        Joiner.joinJars(mapped, universal.asPath.split(File.pathSeparator))
    }
    group = "client"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "Loader"
        attributes["Agent-Class"] = "agent.Agent"
        attributes["Premain-Class"] = "agent.Agent"
        attributes["Can-Retransform-Classes"] = true
        attributes["Can-Redefine-Classes"] = true
    }
}
