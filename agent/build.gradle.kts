plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    api("com.github.SpencerNold:BTCLib:-SNAPSHOT")
}