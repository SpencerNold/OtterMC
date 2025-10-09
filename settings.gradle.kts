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
        maven { url = uri("https://jitpack.io") }
    }
}
