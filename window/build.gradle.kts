// Codebase of this project is shmeh, at some point, I need to go back and pretty it up
// but that day is not today, and for now this is just going to be eye candy on the outside
// and not on the inside

plugins {
    java
    application
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

application {
    mainClass = "net.ottermc.window.Main"
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
}