plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.32"
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
        all {
            repositories {
                maven("https://jitpack.io")
            }
        }
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation(project.dependencies.platform("io.ktor:ktor-bom:1.5.4"))
                implementation("io.ktor:ktor-client-core")
                implementation("io.ktor:ktor-client-serialization")

                implementation("io.github.microutils:kotlin-logging:1.12.5")
                implementation("io.github.microutils:kotlin-logging-common:1.12.5")

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
            }
        }

        jsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js")
            }
        }
    }
}
