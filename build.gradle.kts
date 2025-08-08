import ottermc.InstallTask
import ottermc.InternalUpdateTask
import ottermc.PackageTask

tasks.register<InstallTask>("install") {
    group = "client"
    description = "Prepares to run the client"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-latest:build")
    dependsOn(":plugins:latest:smp:build")
    dependsOn(":wrapper:build")
}

tasks.register<PackageTask>("package") {
    group = "client"
    description = "Builds and packages the client code for production"
    dependsOn(":universal:build")
    dependsOn(":client-v1.8.9:build")
    dependsOn(":plugins:v1.8.9:pvp:build")
    dependsOn(":client-latest:build")
    dependsOn(":plugins:latest:smp:build")
    dependsOn(":wrapper:build")
}

// for internal use ONLY, constants and mappings, jars, and launcher WILL NOT be updated
// ex: ./gradlew internalupdate --clientversion="1.21.3->1.21.4"
tasks.register<InternalUpdateTask>("internalupdate")

tasks.register("temp") {
    val allFiles: List<File> by lazy {
        project.rootDir.walkTopDown().toList()
    }
    val list = mutableListOf<String>()
    for (f in allFiles) {
        if (f.name.contains("1.21.4")) {
            if (f.isDirectory) {
                list.add("Directory: " + f.absolutePath)
            } else {
                list.add("File: " + f.absolutePath)
            }
        }
        if (f.isFile) {
            f.bufferedReader().useLines { lines ->
                if (lines.any { it.contains("1.21.4") || it.contains("1_21_4") }) {
                    list.add("Contains: " + f.absolutePath)
                }
            }
        }
    }
    list.sort()
    for (s in list) {
        println(s)
    }
}
