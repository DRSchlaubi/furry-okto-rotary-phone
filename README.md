# Furry okto rotary phone

Furry okto rotary phone or FORP for short is a Kotlin library for fetching, finding, parsing and
analyzing JVM exception stacktraces

# Status

Not done LOL

# Contents

- [Modules](#modules)
- [Documentation](#documentation)
- [Download (BOM)](#download)

# Modules

- [core](https://github.com/DRSchlaubi/furry-okto-rotary-phone/tree/main/forp-core) - Stack trace
  model and parsing APIs
- [find](https://github.com/DRSchlaubi/furry-okto-rotary-phone/tree/main/forp-find) - Search for
  multiple stack traces within the same input

# Documentation

You can find our docs here: [fopr.schlau.bi](https://fopr.schlau.bi)

# Download

```kotlin
repositories {
    maven("https://schlaubi.jfrog.io/artifactory/forp/")
}

dependencies {
    implementation("dev.schlaubi.forp:forp-<module>:1.0.0-SNAPSHOT")
}
```
