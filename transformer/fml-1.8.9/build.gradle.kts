plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    implementation(project(":transformer"))
    implementation(files("libs/launchwrapper-1.12.jar"))
}