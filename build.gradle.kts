import ottermc.InstallTask

tasks.register<InstallTask>("install") {
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":client:build")
    dependsOn(":versions:agentic:build")
    dependsOn(":versions:vanilla-1.8.9:build")
    dependsOn(":versions:vanilla-1.21.10:build")
    dependsOn(":versions:agentic:build")
    dependsOn(":wrapper:build")
    dependsOn(":window:build")
}