import ottermc.Compiler
import ottermc.Launcher
import ottermc.Remapper

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
	implementation("com.google.code.gson:gson:2.2.4")
	implementation("com.google.guava:guava:17.0")
	implementation("org.lwjgl.lwjgl:lwjgl:2.9.2")
	implementation("org.lwjgl.lwjgl:lwjgl_util:2.9.2")
	
	/* DEPRECATED! */
	implementation("org.ow2.asm:asm:9.6")

	// mc-clean.jar is a the 1.8.9 Minecraft client jar
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
		Launcher.launch(client)
	}
	group = "client"
	description = "Runs the modified game client."
	dependsOn("build")
}

tasks.named("build") {
	doLast {
		val client = file("build/libs/OtterMC.jar")
		Compiler.compile(client)
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