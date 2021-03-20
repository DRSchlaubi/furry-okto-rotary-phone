# forp-bom

A BOM or Bill of Materials allows you to share the same version between different modules

# Download 

```kotlin
repositories {
    maven("https://schlaubi.jfrog.io/artifactory/forp/")
}

dependencies {
    implementation(platform("dev.schlaubi.forp:forp-bom:1.0.0-SNAPSHOT"))
    implementation("dev.schlaubi.forp:forp-<module1>")
    implementation("dev.schlaubi.forp:forp-<module2>")
}
```
