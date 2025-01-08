import ottermc.Installer
import ottermc.Packager

tasks.register("install") {
    doLast {
        Installer.install(rootProject.projectDir)
    }
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-v1.21.3:build")
    dependsOn(":plugins:vlatest:smp:build")
    dependsOn(":wrapper:build")
}

tasks.register("package") {
    doLast {
        Packager.runPackageCode(project.logger, rootProject.projectDir)
    }
    group = "client"
    description = "Builds and packages the client code for production"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-v1.21.3:build")
    dependsOn(":plugins:vlatest:smp:build")
    dependsOn(":wrapper:build")
}
