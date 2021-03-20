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
- [fetch](https://github.com/DRSchlaubi/furry-okto-rotary-phone/tree/main/forp-fetch) - Search for
  stacktraces in hastebin links, images and files

# Documentation

You can find our docs here: [fopr.schlau.bi](https://fopr.schlau.bi)

# Download

### Gradle (Kotlin)

```kotlin
repositories {
    maven("https://schlaubi.jfrog.io/artifactory/forp/")
}

dependencies {
    implementation("dev.schlaubi.forp:forp-<module>:1.0.0-SNAPSHOT")
}

// Or MPP:

sourceSets {
    commonMain {
        repositories {
            maven("https://schlaubi.jfrog.io/artifactory/forp/")
        }

        dependencies {
            implementation("dev.schlaubi.forp:forp-<module>:1.0.0-SNAPSHOT")
        }
    }
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven {
        url "https://schlaubi.jfrog.io/artifactory/forp/"
    }
}

dependencies {
    implementation 'dev.schlaubi.forp:forp-<module>:1.0.0-SNAPSHOT'
}

// Or MPP:

sourceSets {
    commonMain {
        repositories {
            repositories {
                maven {
                    url "https://schlaubi.jfrog.io/artifactory/forp/"
                }
            }

            dependencies {
                implementation 'dev.schlaubi.forp:forp-<module>:1.0.0-SNAPSHOT'
            }
        }
    }
}
```

# Maven

```xml

<repositories>
  <repository>
    <repository>
      <id>forp-repo</id>
      <url>https://schlaubi.jfrog.io/artifactory/forp/</url>
    </repository>
  </repository>
</repositories>

<dependencies>
<dependency>
  <groupId>dev.schlaubi.forp</groupId>
  <artifactId><!--module--></artifactId>
  <version>0.7.0-SNAPSHOT</version>
</dependency>
</dependencies>

  <!-- No MPP! See https://discuss.kotlinlang.org/t/fullstack-kotlin-with-maven/16008/2 -->
```
