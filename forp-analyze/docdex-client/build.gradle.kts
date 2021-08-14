plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
            }
        }
    }

    js(BOTH) {
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                implementation(project.dependencies.platform("io.ktor:ktor-bom:1.6.2"))
                implementation("io.ktor:ktor-client-core")
                implementation("io.ktor:ktor-client-serialization")

                implementation("io.github.microutils:kotlin-logging:2.0.10")

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
            repositories {
                // See https://github.com/Kotlin/kotlinx-nodejs/issues/16
                @Suppress("DEPRECATION")
                jcenter()
            }
            
            dependencies {
                implementation("io.ktor:ktor-client-js")
            }
        }
    }
}
