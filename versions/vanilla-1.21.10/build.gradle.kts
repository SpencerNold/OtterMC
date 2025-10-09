import ottermc.*

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
    // Game Dependencies
    implementation("org.lwjgl:lwjgl-glfw:3.3.3")
    implementation("it.unimi.dsi:fastutil:8.5.15")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.netty:netty-all:4.1.118.Final")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.1")

    // Client dependencies
    implementation(files("libs/mc-clean.jar"))
    implementation(project(":versions:agentic"))
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/vanilla-1.21.10.jar")
        val mapped = Compiler.compile(client, Constants.VERSION_1_21_10)
        val agentic = file("../agentic/build/libs/agentic-joined.jar")
        val joined = Joiner.joinJars(mapped, listOf(agentic.absolutePath))
        VersionController.handle(joined, 65)
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