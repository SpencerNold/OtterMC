rootProject.name = "OtterMC"

include("wrapper")
include("universal")
include("transformer")
include("client-v1.8.9")
include("client-latest")

include("plugins:v1.8.9:pvp")
include("plugins:v1.8.9:pvp-export")

include("plugins:latest:smp")
include("plugins:latest:smp-export")

include("distributors:profiler")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
