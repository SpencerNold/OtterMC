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