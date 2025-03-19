// Export plugin, meant for updating plugins at runtime (future only!)

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ottermc.Compiler
import ottermc.Constants
import ottermc.BuildTool
import ottermc.KotlinPackageTask

plugins {
    kotlin("jvm") version "1.9.22"
}

tasks.named("build") {
    doLast {
        val client = file("build/libs/export.jar")
        Compiler.compile(client, Constants.VERSION_1_8_9)
    }
    group = "plugins"
}

tasks.register<KotlinPackageTask>("package") {
    dependsOn("build")
    group = "plugins"
}

tasks.register("install") {
    doLast {
        val client = file("build/libs/export-remapped-joined.jar")
        val dir = File(BuildTool.getMinecraftDirectory(), "ottermc" + File.separator + "plugins")
        BuildTool.copy(client, File(dir, "export.jar"))
    }
    dependsOn("package")
    group = "plugins"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

dependencies {
    implementation(project(":client-v1.8.9"))
    implementation(kotlin("stdlib"))
}
