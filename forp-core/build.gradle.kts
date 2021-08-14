plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

apply(from = "../publishing.gradle.kts")

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
                freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
                jvmTarget = "11"
            }
        }
    }

    js(BOTH) {
        nodejs()
    }

    explicitApi()

    sourceSets {
        all {
            repositories {
                mavenCentral()
                maven("https://jitpack.io")
            }

            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("dev.schlaubi.forp.core.annotation.ForpInternals")
        }


        @Suppress("UNUSED_VARIABLE") // The common-main shortcuts yells at you
        val commonMain by getting {
            dependencies {
                api(project(":forp-parser"))
                api(project(":forp-parser-api"))
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
