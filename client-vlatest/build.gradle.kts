import ottermc.Compiler
import ottermc.Launcher
import ottermc.Remapper
import ottermc.Constants

plugins {
    eclipse
    idea
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Minecraft Dependencies

    /* DEPRECATED! */
    implementation("org.ow2.asm:asm:9.6")

    // mc-clean.jar is the 1.8.9 Minecraft client jar
    // deobfuscated with the 1.8.9 MCP mappings
    implementation(files("libs/mc-clean.jar"))
}

java {
    setSourceCompatibility(JavaVersion.VERSION_1_8)
    setTargetCompatibility(JavaVersion.VERSION_1_8)
}

tasks.register("remap") {
    doLast {
        Remapper.remap();
    }
    group = "client"
    description = "Prepares the game jar for the gradle build."
}

tasks.register("run") {
    doLast {
        val client = file("build/libs/OtterMC-remapped.jar")
        Launcher.launch(client, Constants.VERSION_LATEST)
    }
    group = "client"
    description = "Runs the modified game client."
    dependsOn("build")
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/client-vlatest.jar")
        Compiler.compile(client, Constants.VERSION_LATEST)
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