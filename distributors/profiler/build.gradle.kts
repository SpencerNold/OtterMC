plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}