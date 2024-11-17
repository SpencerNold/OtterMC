import ottermc.Installer

tasks.register("install") {
    doLast {
        Installer.install(rootProject.projectDir)
    }
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":client-vlatest:build")
    dependsOn(":wrapper:build")
}