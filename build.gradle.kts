import ottermc.InstallTask
import ottermc.PackageTask

tasks.register<InstallTask>("install") {
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":client:build")
    dependsOn(":versions:agentic:build")
    dependsOn(":versions:vanilla-1.8.9:build")
    dependsOn(":versions:vanilla-1.21.10:build")
    dependsOn(":versions:fabric-1.21.8-patch:build")
    dependsOn(":versions:fabric-1.21:build")
    dependsOn(":wrapper:build")
    dependsOn(":window:build")
}

tasks.register<PackageTask>("distribute") {
    group = "client"
    description = "Generates an installer for the client"
    dependsOn(":client:build")
    dependsOn(":versions:agentic:build")
    dependsOn(":versions:vanilla-1.8.9:build")
    dependsOn(":versions:vanilla-1.21.10:build")
    dependsOn(":versions:fabric-1.21.8-patch:build")
    dependsOn(":versions:fabric-1.21:build")
    dependsOn(":wrapper:build")
    dependsOn(":window:build")
    dependsOn(":distributors:profiler:build")
}