plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.32"
    id("org.jetbrains.dokka")
}

apply(from = "../../publishing.gradle.kts")

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
        all {
            repositories {
                maven("https://schlaubi.jfrog.io/artifactory/forp/")
            }

            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }

        commonMain {
            dependencies {
                api(project(":forp-analyze:forp-analyze-api"))
                api(project(":forp-analyze:docdex-client-api"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":forp-test-helper"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
            }
        }

        jsTest {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
