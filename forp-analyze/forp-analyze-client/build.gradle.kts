plugins {
    kotlin("multiplatform")
    id("kotlinx-atomicfu") version "0.16.1"
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
                maven("https://schlaubi.jfrog.io/artifactory/forp/")
                mavenCentral()
            }

            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }

        commonMain {
            dependencies {
                implementation(project.dependencies.platform("io.ktor:ktor-bom:1.5.2"))
                implementation("io.ktor:ktor-client-core")
                implementation("io.ktor:ktor-client-serialization")
                implementation("io.github.microutils:kotlin-logging:1.12.5")
                implementation("io.github.microutils:kotlin-logging-common:1.12.5")
                implementation("org.jetbrains.kotlinx:atomicfu:0.15.2")

                api(project(":forp-analyze:forp-analyze-api"))

                api(project(":forp-analyze:forp-analyze-remote-api"))
                api(project(":forp-analyze:docdex-client-api"))
            }
        }

        jsMain {
            dependencies {
                implementation("io.github.microutils:kotlin-logging-js:1.12.5")

            }
        }
    }
}
