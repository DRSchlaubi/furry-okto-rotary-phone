plugins {
    kotlin("multiplatform")
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

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
                api(project(":forp-analyze:forp-analyze-api"))
            }
        }
    }
}
