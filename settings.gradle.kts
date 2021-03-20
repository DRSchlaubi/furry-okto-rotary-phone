rootProject.name = "forp"

include("forp-core")
include("forp-find")
include("forp-test-helper")
include("forp-parser")
include("examples")

pluginManagement {
    resolutionStrategy {
        repositories {
            jcenter()
            gradlePluginPortal()
            maven("https://jitpack.io")
        }
    }
}
include("forp-fetch")
include("forp-bom")
