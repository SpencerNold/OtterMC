import ottermc.Remapper
import ottermc.Compiler
import ottermc.Launcher

plugins {
	eclipse
	idea
	java
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.code.gson:gson:2.2.4")
	implementation("com.google.guava:guava:17.0")
	implementation("org.lwjgl.lwjgl:lwjgl:2.9.2")
	implementation("org.lwjgl.lwjgl:lwjgl_util:2.9.2")
	
	
	implementation("org.ow2.asm:asm:9.6")
	
	// mc-clean.jar is a the 1.8.9 Minecraft client jar
	// deobfuscated with the 1.8.9 MCP mappings
	implementation(files("libs/mc-clean.jar"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.register("remap") {
	doLast {
		Remapper.remap();
	}
	group = "client"
	description = "Prepares the game jar for the gradle build."
}

tasks.register("runClient") {
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
		attributes["Premain-Class"] = "agent.Agent"
		attributes["Can-Retransform-Classes"] = true
	}
}