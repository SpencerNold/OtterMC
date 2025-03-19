plugins {
    java
    application
    distribution
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
}

fun getProjectOutputPath(project: String, jar: String): String {
    val path = project(project).layout.buildDirectory.asFile.get().absolutePath
    return path + File.separator + "libs" + File.separator + jar
}

distributions {
    val list = listOf(
        getProjectOutputPath(":wrapper", "wrapper.jar"),
        getProjectOutputPath(":client-v1.8.9", "client-v1.8.9-remapped-joined.jar"),
        getProjectOutputPath(":client-v1.21.4", "client-v1.21.4-remapped-joined.jar"),
        getProjectOutputPath(":plugins:v1.8.9:pvp", "pvp-remapped.jar"),
        getProjectOutputPath(":plugins:v1.21.4:smp", "smp-remapped.jar")
    )
    main {
        contents {
            from(list)
        }
    }
}

tasks.named("distTar") {
    mustRunAfter(":wrapper:jar")
    mustRunAfter(":client-v1.8.9:build")
    mustRunAfter(":client-v1.21.4:build")
    mustRunAfter(":plugins:v1.8.9:pvp:build")
    mustRunAfter(":plugins:v1.21.4:smp:build")
}

tasks.named("distZip") {
    mustRunAfter(":wrapper:jar")
    mustRunAfter(":client-v1.8.9:build")
    mustRunAfter(":client-v1.21.4:build")
    mustRunAfter(":plugins:v1.8.9:pvp:build")
    mustRunAfter(":plugins:v1.21.4:smp:build")
}

application {
    mainClass = "io.github.ottermc.Main"
}
