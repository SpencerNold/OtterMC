import ottermc.InstallTask
import ottermc.PackageTask
import ottermc.VersionTask

tasks.register<InstallTask>("install") {
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":c2:build")
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":plugins:v1.8.9:pvp-export:build")
    dependsOn(":client-latest:build")
    dependsOn(":plugins:latest:smp:build")
    dependsOn(":plugins:latest:smp-export:build")
    dependsOn(":wrapper:build")
}

tasks.register<PackageTask>("package") {
    group = "client"
    description = "Builds and packages the client code for production"
    dependsOn(":c2:build")
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":plugins:v1.8.9:pvp-export:build")
    dependsOn(":client-latest:build")
    dependsOn(":plugins:latest:smp:build")
    dependsOn(":plugins:latest:smp-export:build")
    dependsOn(":wrapper:build")
}

tasks.register<VersionTask>("version")