plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "dev.schlaubi.forp"
version = "0.0.2"


repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://schlaubi.jfrog.io/forp")
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:1.6.2"))
    implementation("io.ktor", "ktor-server-core")
    implementation("io.ktor", "ktor-auth")
    implementation("io.ktor", "ktor-locations")
    implementation("io.ktor", "ktor-serialization")
    implementation("io.ktor", "ktor-websockets")
    implementation("io.ktor", "ktor-server-cio")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")
    implementation("io.github.microutils", "kotlin-logging", "2.0.10")

    implementation("com.github.uchuhimo.konf", "konf-toml", "master-SNAPSHOT")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.13.+")

    implementation(project(":forp-analyze:docdex-client"))
    implementation(project(":forp-analyze:forp-analyze-remote-api"))
    implementation(project(":forp-analyze:forp-analyze-core"))

    implementation(platform("com.google.cloud:libraries-bom:20.9.0"))
    implementation("com.google.cloud", "google-cloud-vision")

    testImplementation("io.ktor", "ktor-server-tests")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs =
                freeCompilerArgs + "-Xopt-in=io.ktor.locations.KtorExperimentalLocationsAPI" + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

application {
    mainClass.set("dev.schlaubi.forp.analyze.server.ApplicationKt")
}
