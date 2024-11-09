plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.6")
    implementation(files("libs/tools.jar"))
}