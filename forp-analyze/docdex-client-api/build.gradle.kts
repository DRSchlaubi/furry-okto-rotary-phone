plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.32"
}

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
                useIR = true
            }
        }
    }

    js(LEGACY) {
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation(project(":forp-analyze:forp-analyze-api"))
            }
        }
    }
}
