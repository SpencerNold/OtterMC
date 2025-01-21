rootProject.name = "OtterMC"

include("installercli")

include("transformer")

include("wrapper")
include("universal")
include("client-v1.8.9")
include("client-v1.21.3")

include("plugins:v1.8.9:pvp")
include("plugins:v1.21.3:smp")

// in build scripts, v<version> is a 'hot' line of code
