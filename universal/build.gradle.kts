plugins {
    `java-library`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

dependencies {
    api("com.github.SpencerNold:KWAF:-SNAPSHOT")
    api(project(":agent"))
    if (JavaVersion.current().isJava8) {
        implementation(files("${System.getProperty("java.home")}/lib/tools.jar"))
    } else {
        implementation(files("libs/tools.jar"))
    }
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
