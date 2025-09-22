plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8);
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
}