plugins {
    java
    application
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(8)
}

application {
    mainClass = "net.ottermc.window.Main"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}