plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

apply(from = "../../publishing.gradle.kts")

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

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
        commonMain {
            repositories {
                maven("https://schlaubi.jfrog.io/artifactory/forp/")
            }

            dependencies {
                api(project(":forp-fetch"))
            }
        }

    }
}
