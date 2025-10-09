import ottermc.Joiner

plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

val kwaf: Configuration by configurations.creating

dependencies {
    kwaf("com.github.SpencerNold:KWAF:-SNAPSHOT")
    for (depend in kwaf.dependencies)
        api(depend)
}

tasks.named("build") {
    doLast {
        val jar = file("build/libs/client.jar")
        Joiner.joinJars(jar, kwaf.asPath.split(File.pathSeparator))
    }
}
