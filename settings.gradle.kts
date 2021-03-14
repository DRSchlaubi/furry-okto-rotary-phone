rootProject.name = "String"
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
