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
        all {
            repositories {
                maven("https://jitpack.io")
            }
        }
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation(project.dependencies.platform("io.ktor:ktor-bom:1.5.2"))
                api(project(":forp-analyze:forp-analyze-api"))
                api(project(":forp-analyze:docdex-client-api"))
                compileOnly(project(":forp-analyze:forp-analyze-core"))
            }
        }

        jvmMain {
            repositories {
                maven("https://oss.sonatype.org/content/repositories/snapshots")
            }

            dependencies {
                implementation("io.ktor:ktor-client-okhttp")
                implementation("dev.kord:kord-core:0.7.0-SNAPSHOT")
            }

        }

        jsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js")
            }
        }
    }
}
