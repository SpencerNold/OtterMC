import ottermc.Joiner

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8);
}

val universal: Configuration by configurations.creating

dependencies {
    implementation(files("libs/launchwrapper-1.12.jar"))
    implementation(project(":agent"))
    universal(project(":addons:universal"))
    for (depend in universal.dependencies)
        implementation(depend)
}

tasks.named("build") {
    doLast {
        val addon = file("build/libs/forge.jar")
        Joiner.joinJars(addon, universal.asPath.split(File.pathSeparator))
    }
    dependsOn(":addons:universal:build")
    group = "addon"
}

