import ottermc.InstallTask
import ottermc.InternalUpdateTask
import ottermc.PackageTask

tasks.register<InstallTask>("install") {
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-v1.21.4:build")
    dependsOn(":plugins:v1.21.4:smp:build")
    dependsOn(":wrapper:build")
}

tasks.register<PackageTask>("package") {
    group = "client"
    description = "Builds and packages the client code for production"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-v1.21.4:build")
    dependsOn(":plugins:v1.21.4:smp:build")
    dependsOn(":wrapper:build")
}

// for internal use ONLY, constants and mappings, jars, and launcher WILL NOT be updated
// ex: ./gradlew internalupdate --clientversion="1.21.3->1.21.4"
tasks.register<InternalUpdateTask>("internalupdate")
