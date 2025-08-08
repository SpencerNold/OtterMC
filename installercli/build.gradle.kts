plugins {
    java
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.register<Jar>("buildCompleteJar") {
    archiveClassifier.set("complete")
    from(sourceSets.main.get().output)

    from(zipTree(configurations.runtimeClasspath.get().filter {
        it.name.startsWith("gson")
    }.first()))

    from(getProjectPath(":wrapper"))
    mustRunAfter(":wrapper:jar")

    from(getProjectPath(":client-v1.8.9"))
    mustRunAfter(":client-v1.8.9:build")

    from(getProjectPath(":client-latest"))
    mustRunAfter(":client-latest:build")

    from(getProjectPath(":plugins:v1.8.9:pvp"))
    mustRunAfter(":plugins:v1.8.9:pvp:build")

    from(getProjectPath(":plugins:latest:smp"))
    mustRunAfter(":plugins:latest:smp:build")

    manifest {
        attributes(
            "Main-Class" to "io.github.ottermc.Main"
        )
    }

    dependsOn("build")
}

fun getProjectPath(name: String): File {
    return project(name).tasks.named<Jar>("jar").map { it.archiveFile.get().asFile }.get()
}
