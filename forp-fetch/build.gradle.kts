plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.31"
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
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("dev.schlaubi.forp.core.annotation.ForpInternals")

            repositories {
                mavenCentral()
                maven("https://jitpack.io")
            }

            dependencies {
                api(project.dependencies.platform("io.ktor:ktor-bom:1.5.2"))
            }
        }

        commonMain {
            dependencies {
                api(project(":forp-find"))
                api("io.ktor:ktor-io")
                api("io.ktor:ktor-client-core")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                api("io.ktor:ktor-client-serialization")
            }
        }

        jvmMain {
            repositories {
                google()
            }

            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")
                api(project.dependencies.platform("com.google.cloud:libraries-bom:19.2.1"))
                implementation("com.google.cloud:google-cloud-vision")
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
