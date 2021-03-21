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
