plugins {
    java
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":forp-fetch"))


    implementation(platform("com.google.cloud:libraries-bom:19.2.1"))
    implementation("com.google.cloud:google-cloud-vision")
}

configure<JavaPluginConvention> {
    @Suppress("UnstableApiUsage")
    sourceCompatibility = JavaVersion.VERSION_15
}
