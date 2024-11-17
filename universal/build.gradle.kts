plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.7.1")
    if (JavaVersion.current().isJava8) {
        implementation(files("${System.getProperty("java.home")}/lib/tools.jar"))
    } else {
        implementation(files("libs/tools.jar"))
    }
}