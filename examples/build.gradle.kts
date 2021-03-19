plugins {
    java
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":forp-find"))
}

configure<JavaPluginConvention> {
    @Suppress("UnstableApiUsage")
    sourceCompatibility = JavaVersion.VERSION_15
}
