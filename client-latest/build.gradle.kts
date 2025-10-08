import ottermc.*

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
    api("io.netty:netty-all:4.1.118.Final")
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
        var paths = universal.asPath.split(File.pathSeparator)
        paths = UniversalDependencyHandler.filter(paths)
        val joined = Joiner.joinJars(mapped, paths)
        VersionController.handle(joined, 65)
    }
}