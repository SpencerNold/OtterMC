rootProject.name = "OtterMC"

include("installercli")

include("wrapper")
include("universal")
include("client-v1.8.9")
include("client-v1.21.4")

include("plugins:v1.8.9:pvp")
include("plugins:v1.8.9:export")

include("plugins:v1.21.4:smp")
include("plugins:v1.21.4:export")

// in build scripts, v<version> is a 'hot' line of code

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
