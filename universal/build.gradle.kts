plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    api("com.github.SpencerNold:BTCLib:-SNAPSHOT")
    if (JavaVersion.current().isJava8) {
        implementation(files("${System.getProperty("java.home")}/lib/tools.jar"))
    } else {
        implementation(files("libs/tools.jar"))
    }
}
