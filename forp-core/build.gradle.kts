plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

group = "dev.schlaubi"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

publishing {
    publications {
        filterIsInstance<MavenPublication>().forEach { publication ->
            publication.pom {
                name.set(project.name)
                description.set("Kotlin library which can, fetch, find, parse and analyze JVM exception stacktraces")
                url.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone")

                licenses {
                    license {
                        name.set("Apache-2.0 License")
                        url.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone/blob/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        name.set("Michael Rittmeister")
                        email.set("mail@schlaubi.me")
                        organizationUrl.set("https://michael.rittmeister.in")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/DRSchlaubi/furry-okto-rotary-phone.git")
                    developerConnection.set("scm:git:https://github.com/DRSchlaubi/furry-okto-rotary-phone.git")
                    url.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone")
                }
            }
        }
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
                useIR = true
                jvmTarget = "11"
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

            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("dev.schlaubi.forp.core.annotation.ForpInternals")
        }


        @Suppress("UNUSED_VARIABLE") // The common-main shortcuts yells at you
        val commonMain by getting {
            dependencies {
                api(project(":forp-parser"))
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
