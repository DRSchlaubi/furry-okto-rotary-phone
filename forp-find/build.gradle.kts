plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

group = "dev.schlaubi.forp"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://jitpack.io")
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

    explicitApi()

    sourceSets {
        all {
            repositories {
                mavenCentral()
                maven("https://jitpack.io")
            }

            languageSettings.useExperimentalAnnotation("dev.schlaubi.forp.core.annotation.ForpInternals")
        }

        commonMain {
            dependencies {
                api(project(":forp-core"))
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

configurePublishing()
