plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    implementation(files("libs/launchwrapper-1.12.jar"))
}