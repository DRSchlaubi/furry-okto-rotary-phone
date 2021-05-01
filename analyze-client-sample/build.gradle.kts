plugins {
    kotlin("jvm")
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://schlaubi.jfrog.io/artifactory/forp")
}

dependencies {
    implementation(project(":forp-analyze:forp-analyze-client"))
    implementation("org.slf4j", "slf4j-simple", "1.7.30")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}
