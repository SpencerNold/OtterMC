plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    api("com.github.SpencerNold:KWAF:-SNAPSHOT")
    api(project(":transformer"))
    if (JavaVersion.current().isJava8) {
        implementation(files("${System.getProperty("java.home")}/lib/tools.jar"))
    } else {
        implementation(files("libs/tools.jar"))
    }
    // Loaders
    implementation(project(":transformer:fml-1.8.9"))
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "Loader"
        attributes["Agent-Class"] = "agent.Agent"
        attributes["Premain-Class"] = "agent.Agent"
        attributes["Can-Retransform-Classes"] = true
        attributes["Can-Redefine-Classes"] = true
    }
}
