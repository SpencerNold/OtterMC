rootProject.name = "OtterMC"

include("wrapper")
include("window")

include("client")

include("versions:agentic")
include("versions:vanilla-1.8.9")
include("versions:vanilla-1.21.10")

include("versions:fabric-1.21.8-patch")
include("versions:fabric-1.21")

include("distributors:profiler")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
        // BTCLib and KWAF
        maven { url = uri("https://jitpack.io") }
        // Used by Mojang?
        maven("https://repo.azisaba.net/repository/maven-public/") {
            name = "AzisabaPublic"
        }
    }
}

// FabricMC for versions:fabric-*
pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
        mavenCentral()
    }
}