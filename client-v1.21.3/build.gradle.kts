import ottermc.Compiler
import ottermc.Launcher
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

    // Client dependencies
    universal(project(":universal"))
    for (depend in universal.dependencies)
        api(depend)
    implementation("org.ow2.asm:asm:9.7.1")
    api(files("libs/mc-clean.jar"))

}

tasks.register("attach") {
    doLast {
        val client = file("build/libs/client-v1.21.3-remapped-joined.jar")
        Launcher.attach(client, Constants.VERSION_1_21_3)
    }
    group = "client"
    description = "Attaches the client to a running game client."
    dependsOn("build")
}

tasks.register("run") {
    doLast {
        val client = file("build/libs/client-v1.21.3-remapped-joined.jar")
        Launcher.launch(client, Constants.VERSION_1_21_3)
    }
    group = "client"
    description = "Runs the modified game client."
    dependsOn("build")
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/client-v1.21.3.jar")
        val mapped = Compiler.compile(client, Constants.VERSION_1_21_3)
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