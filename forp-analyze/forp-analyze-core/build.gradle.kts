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

            repositories {
                maven("https://jitpack.io")
            }
        }

        commonMain {
            dependencies {
                api(project(":forp-analyze:forp-analyze-api"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
                implementation("io.github.microutils:kotlin-logging:1.12.5")
                implementation("io.github.microutils:kotlin-logging-common:1.12.5")
            }
        }

        jsMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")
                implementation("io.github.microutils:kotlin-logging-js:1.12.5")
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
