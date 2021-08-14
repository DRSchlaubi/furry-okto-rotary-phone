plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

apply(from = "../../publishing.gradle.kts")

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    js(BOTH) {
        nodejs()
    }

    sourceSets {
        all {
            repositories {
                maven("https://schlaubi.jfrog.io/artifactory/forp/")
            }
        }

        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                implementation(project(":forp-analyze:forp-analyze-api"))
            }
        }
    }
}
