import ottermc.Compiler
import ottermc.Constants
import ottermc.Joiner
import ottermc.RunClientTask

plugins {
	`java-library`
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
	api("com.google.code.gson:gson:2.2.4")
	api("com.google.guava:guava:17.0")
	api("org.lwjgl.lwjgl:lwjgl:2.9.2")
	api("org.lwjgl.lwjgl:lwjgl_util:2.9.2")


	// Client dependencies
	universal(project(":universal"))
	for (depend in universal.dependencies)
		api(depend)
	implementation("org.ow2.asm:asm:9.7.1")
	api(files("libs/mc-clean.jar"))
}

tasks.register("attach") {
	doLast {
		val client = file("build/libs/client-v1.8.9-remapped-joined.jar")
		RunClientTask.attach(client, Constants.VERSION_1_8_9)
	}
	group = "client"
	description = "Attaches the client to a running game client."
	dependsOn("build")
}

tasks.register("run") {
	doLast {
		val client = file("build/libs/client-v1.8.9-remapped-joined.jar")
		RunClientTask.launch(client, Constants.VERSION_1_8_9)
	}
	group = "client"
	description = "Runs the modified game client."
	dependsOn(":install")
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
		attributes["Can-Redefine-Classes"] = true
	}
}
