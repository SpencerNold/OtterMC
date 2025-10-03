import ottermc.*

plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

val common: Configuration by configurations.creating

dependencies {
    implementation(files("libs/launchwrapper-1.12.jar"))
    common(project(":transformer:common"))
    for (depend in common.dependencies)
        implementation(depend)
}

tasks.named("build") {
    doLast {
        val jar = file("build/libs/fml-1.8.9.jar")
        val joined = Joiner.joinJars(jar, common.asPath.split(File.pathSeparator))
        VersionController.handle(joined, 52)
    }
    group = "transformer"
}