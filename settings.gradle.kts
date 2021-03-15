rootProject.name = "forp"
include("forp-core", "forp-find")

pluginManagement {
    resolutionStrategy {
        repositories {
            jcenter()
            gradlePluginPortal()
        }
    }
}
include("forp-test-helper")
