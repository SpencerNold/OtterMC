import ottermc.Compiler
import ottermc.Launcher
import ottermc.Constants
import ottermc.Joiner

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

val universal: Configuration by configurations.creating

dependencies {
    // Game Dependencies

    // Client dependencies
    universal(project(":universal"))
    for (depend in universal.dependencies)
        api(depend)
    implementation("org.ow2.asm:asm:9.7.1")
    implementation(files("libs/mc-clean.jar"))
}

tasks.register("attach") {
    doLast {
        val client = file("build/libs/client-vlatest-remapped-joined.jar")
        Launcher.attach(client, Constants.VERSION_LATEST)
    }
    group = "client"
    description = "Attaches the client to a running game client."
    dependsOn("build")
}

tasks.register("run") {
    doLast {
        val client = file("build/libs/client-vlatest-remapped-joined.jar")
        Launcher.launch(client, Constants.VERSION_LATEST)
    }
    group = "client"
    description = "Runs the modified game client."
    dependsOn("build")
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/client-vlatest.jar")
        val mapped = Compiler.compile(client, Constants.VERSION_LATEST)
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
    }
}