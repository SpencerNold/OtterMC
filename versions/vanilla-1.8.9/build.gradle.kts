import ottermc.*

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    // Game Dependencies
    implementation("com.google.code.gson:gson:2.2.4")
    implementation("com.google.guava:guava:17.0")
    implementation("org.lwjgl.lwjgl:lwjgl:2.9.2")
    implementation("org.lwjgl.lwjgl:lwjgl_util:2.9.2")
    implementation("org.apache.logging.log4j:log4j-core:2.0-beta9")

    // Client dependencies
    implementation(files("libs/mc-clean.jar"))
    implementation(project(":versions:agentic"))
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/vanilla-1.8.9.jar")
        val mc = file("libs/mc-clean.jar")
        val agentic = file("../agentic/build/libs/agentic-joined.jar")
        val common = file("../../client/build/libs/client-joined.jar")
        val mapped = Compiler.compile(client, arrayOf(agentic, common), Constants.VERSION_1_8_9, mc)
        val joined = Joiner.joinJars(mapped, listOf(agentic.absolutePath))
        VersionController.handle(joined, 52)
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