import ottermc.Compiler
import ottermc.Launcher
import ottermc.Joiner
import ottermc.Constants

plugins {
	java
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(8)
}

repositories {
	mavenCentral()
}

val universal: Configuration by configurations.creating

dependencies {
	// Game Dependencies
	implementation("com.google.code.gson:gson:2.2.4")
	implementation("com.google.guava:guava:17.0")
	implementation("org.lwjgl.lwjgl:lwjgl:2.9.2")
	implementation("org.lwjgl.lwjgl:lwjgl_util:2.9.2")

	// Client dependencies
	universal(project(":universal"))
	for (depend in universal.dependencies)
		implementation(depend)
	implementation("org.ow2.asm:asm:9.6")
	implementation(files("libs/mc-clean.jar"))
}

tasks.register("run") {
	doLast {
		val client = file("build/libs/client-v1.8.9-remapped-joined.jar")
		Launcher.launch(client, Constants.VERSION_1_8_9)
	}
	group = "client"
	description = "Runs the modified game client."
	dependsOn("build")
}

tasks.named("build") {
	doLast {
		val client = file("build/libs/client-v1.8.9.jar")
		val mapped = Compiler.compile(client, Constants.VERSION_1_8_9)
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