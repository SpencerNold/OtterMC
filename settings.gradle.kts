rootProject.name = "OtterMC"

include("wrapper")
include("window")

include("client")

include("versions:agentic")
include("versions:vanilla-1.8.9")
include("versions:vanilla-1.21.10")

include("distributors:profiler")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
