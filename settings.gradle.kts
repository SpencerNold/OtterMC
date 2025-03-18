rootProject.name = "OtterMC"

include("installercli")

include("transformer")

include("wrapper")
include("universal")
include("client-v1.8.9")
include("client-v1.21.4")

include("plugins:v1.8.9:pvp")
include("plugins:v1.8.9:export")

include("plugins:v1.21.4:smp")
include("plugins:v1.21.4:export")

// in build scripts, v<version> is a 'hot' line of code
