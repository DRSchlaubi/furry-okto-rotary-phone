plugins {
    kotlin("multiplatform")
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
                mavenCentral()
                maven("https://jitpack.io")
            }
        }

        commonMain {
            dependencies {
                implementation(project(":forp-core"))
                api(kotlin("test-common"))
            }
        }

        jvmMain {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }

        jsMain {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
