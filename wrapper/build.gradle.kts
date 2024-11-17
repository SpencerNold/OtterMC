plugins {
    java
    application
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8);
}

application {
    mainClass = "io.github.ottermc.Wrapper"
}