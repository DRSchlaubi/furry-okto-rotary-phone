import com.strumenta.antlrkotlin.gradleplugin.AntlrKotlinTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.strumenta.antlr-kotlin", "antlr-kotlin-gradle-plugin", "0951069063")
    }
}

plugins {
    kotlin("multiplatform")
}

apply(from = "../publishing.gradle.kts")

group = "dev.schlaubi.forp"
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    targets.all {
        compilations.all {
            kotlinOptions {
                suppressWarnings = true
            }
        }
    }
    
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    js(LEGACY) {
        nodejs()
    }

    sourceSets {
        all {
            repositories {
                mavenCentral()
                maven("https://jitpack.io")
            }
        }

        val commonAntlr by creating {
            dependencies {
                api(kotlin("stdlib-common"))
                api("com.strumenta.antlr-kotlin:antlr-kotlin-runtime:0951069063")
            }

            kotlin.srcDir("build/generated-src/commonAntlr/kotlin")
        }

        @Suppress("UNUSED_VARIABLE") // The common-main shortcuts yells at you
        val commonMain by getting {
            dependsOn(commonAntlr)
        }
    }
}

tasks {
    @Suppress("UnstableApiUsage") val generateGrammarSource =
        task<AntlrKotlinTask>("generateKotlinCommonGrammarSource") {
            antlrClasspath = configurations.detachedConfiguration(
                project.dependencies.create(antlrKotlin("target"))
            )

            maxHeapSize = "64m"
            packageName = "dev.schlaubi.forp.core.parser"
            arguments = listOf("-visitor")

            source = project.objects
                .sourceDirectorySet("antlr", "antlr")
                .srcDir("src/commonAntlr/antlr").apply {
                    include("*.g4")
                }

            outputDirectory = project.file("build/generated-src/commonAntlr/kotlin")
        }

    withType<KotlinCompile> {
        dependsOn(generateGrammarSource)
    }
}
