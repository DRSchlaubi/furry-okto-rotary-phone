plugins {
    java
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":forp-fetch"))

    implementation(platform("com.google.cloud:libraries-bom:20.9.0"))
    implementation("com.google.cloud", "google-cloud-vision")
    implementation("org.slf4j", "slf4j-simple", "1.7.30")
}

java {
    @Suppress("UnstableApiUsage")
    sourceCompatibility = JavaVersion.VERSION_16
}
