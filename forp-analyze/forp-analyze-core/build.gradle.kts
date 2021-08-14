plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

apply(from = "../../publishing.gradle.kts")

group = "me.schlaubi.forp"

repositories {
    mavenCentral()
    maven("https://schlaubi.jfrog.io/forp")
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
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")

            repositories {
                maven("https://jitpack.io")
            }
        }

        commonMain {
            dependencies {
                api(project(":forp-analyze:forp-analyze-api"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                implementation("io.github.microutils:kotlin-logging:2.0.10")
            }
        }

        jsMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
            }
        }

        jsTest {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
