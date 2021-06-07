plugins {
    kotlin("multiplatform") version "1.5.10" apply false
    id("org.jetbrains.dokka") version "1.4.30"
    id("org.ajoberstar.git-publish") version "2.1.3"
    signing
    `maven-publish`
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }
}

tasks {
    dokkaHtmlMultiModule {
        outputDirectory.set(rootProject.file("docs"))
    }

    val docs = task<Copy>("createDocsIndex") {
        dependsOn(dokkaHtmlMultiModule)
        val outputDirectory = dokkaHtmlMultiModule.get().outputDirectory.get()
        from(outputDirectory, rootProject.projectDir)
        include("-modules.html", "CNAME")
        into(outputDirectory)

        rename("-modules.html", "index.html")
    }

    gitPublishPush {
        dependsOn(docs)
    }
}

subprojects {
    group = rootProject.group

    tasks {
        withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
            dokkaSourceSets {
                configureEach {
                    perPackageOption {
                        matchingRegex.set("dev.schlaubi.forp.core.parser") // No ANTLR docs
                        suppress.set(true)
                    }
                }

                val map = asMap

                if (map.containsKey("jsMain")) {
                    named("jsMain") {
                        displayName.set("JS")
                    }
                }

                if (map.containsKey("jvmMain")) {
                    named("jvmMain") {
                        jdkVersion.set(11)
                        displayName.set("JVM")
                    }
                }
            }
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = freeCompilerArgs +
                        "-Xopt-in=dev.schlaubi.forp.core.annotation.ForpInternals"
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}

configure<org.ajoberstar.gradle.git.publish.GitPublishExtension> {
    repoUri.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone.git")
    branch.set("gh-pages")

    contents {
        from(file("docs"))
    }

    commitMessage.set("Update Docs")
}
