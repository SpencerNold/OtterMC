import ottermc.VersionTask
import ottermc.InstallTask
import ottermc.PackageTask

tasks.register<InstallTask>("install") {
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-latest:build")
    dependsOn(":plugins:latest:smp:build")
    dependsOn(":wrapper:build")
}

tasks.register<PackageTask>("package") {
    group = "client"
    description = "Builds and packages the client code for production"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-latest:build")
    dependsOn(":plugins:latest:smp:build")
    dependsOn(":wrapper:build")
}

tasks.register<VersionTask>("version")