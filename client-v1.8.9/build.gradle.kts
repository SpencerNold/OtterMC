import ottermc.Compiler
import ottermc.Constants
import ottermc.Joiner
import ottermc.RunClientTask
import ottermc.UniversalDependencyHandler
import ottermc.VersionController

plugins {
	`java-library`
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(8)
}

val universal: Configuration by configurations.creating

dependencies {
	// Game Dependencies
	api("com.google.code.gson:gson:2.2.4")
	api("com.google.guava:guava:17.0")
	api("org.lwjgl.lwjgl:lwjgl:2.9.2")
	api("org.lwjgl.lwjgl:lwjgl_util:2.9.2")
	implementation("org.apache.logging.log4j:log4j-core:2.0-beta9")


	// Client dependencies
	universal(project(":universal"))
	for (depend in universal.dependencies)
		api(depend)
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
		var paths = universal.asPath.split(File.pathSeparator)
		paths = UniversalDependencyHandler.filter(paths)
		val joined = Joiner.joinJars(mapped, paths)
		VersionController.handle(joined, 52)
	}
	group = "client"
}