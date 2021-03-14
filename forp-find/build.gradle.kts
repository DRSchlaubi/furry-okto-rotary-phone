plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":forp-core"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(project(":forp-test-helper"))
    testRuntimeOnly("org.junit.jupiter","junit-jupiter-engine", "5.7.1")
}

kotlin {
    explicitApi()
}
