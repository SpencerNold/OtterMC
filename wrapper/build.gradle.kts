import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8);
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
}

tasks.withType<ShadowJar> {
    exclude("META-INF/**")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}