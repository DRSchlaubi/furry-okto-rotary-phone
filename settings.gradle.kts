rootProject.name = "forp"

include("forp-core")
include("forp-find")
include("forp-test-helper")
include("forp-parser")
include("examples")
include("forp-fetch")
include("forp-bom")

pluginManagement {
    resolutionStrategy {
        repositories {
            jcenter()
            gradlePluginPortal()
            maven("https://jitpack.io")
        }
    }
}
include("forp-parser-api")
include("forp-analyze")
include("forp-analyze-api")
include("forp-analyze:forp-analyze-api")
findProject(":forp-analyze:forp-analyze-api")?.name = "forp-analyze-api"
include("forp-analyze:forp-analyze-remote-api")
findProject(":forp-analyze:forp-analyze-remote-api")?.name = "forp-analyze-remote-api"
include("forp-analyze:forp-analyze-core")
findProject(":forp-analyze:forp-analyze-core")?.name = "forp-analyze-core"
