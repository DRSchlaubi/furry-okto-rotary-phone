import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    antlr
}

group = "dev.schlaubi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr", "antlr4", "4.9.2")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(project(":forp-test-helper"))
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.7.1")
}

kotlin {
    explicitApi()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    generateGrammarSource {
        outputDirectory =
            File(project.buildDir, "generated-src/antlr/main/dev/schlaubi/forp/core/parser")
        arguments = arguments + listOf("-visitor", "-package", "dev.schlaubi.forp.core.parser")
    }
}
