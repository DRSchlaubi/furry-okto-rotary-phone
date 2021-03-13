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
    testRuntimeOnly("org.junit.jupiter","junit-jupiter-engine", "5.7.1")
}

kotlin {
    explicitApi()
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "15"
            useIR = true
            freeCompilerArgs = freeCompilerArgs +
                    "-Xopt-in=kotlin.RequiresOptIn" +
                    "-Xopt-in=dev.schlaubi.fopr.core.annotation.FoprInternals"
        }
    }

    generateGrammarSource {
        outputDirectory =
            File(project.buildDir, "generated-src/antlr/main/dev/schlaubi/fopr/parser")
        arguments = arguments + listOf("-visitor", "-package", "dev.schlaubi.fopr.parser")
    }
}
