plugins {
    kotlin("jvm")
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":forp-core"))
    implementation(kotlin("test"))
}
