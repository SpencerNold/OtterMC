import ottermc.Joiner

plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

val btcLib: Configuration by configurations.creating

dependencies {
    api(project(":client"))
    btcLib("com.github.SpencerNold:BTCLib:-SNAPSHOT")
    for (depend in btcLib.dependencies)
        api(depend)
    if (JavaVersion.current().isJava8) {
        implementation(files("${System.getProperty("java.home")}/lib/tools.jar"))
    } else {
        implementation(files("libs/tools.jar"))
    }
}

tasks.named("build") {
    doLast {
        val jar = file("build/libs/agentic.jar")
        Joiner.joinJars(jar, btcLib.asPath.split(File.pathSeparator))
    }
}