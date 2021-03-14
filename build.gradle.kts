plugins {
    kotlin("multiplatform") version "1.4.31" apply false
    id("org.jetbrains.dokka") version "1.4.20"
    id("org.ajoberstar.git-publish") version "2.1.3"
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

tasks {
    dokkaHtmlMultiModule {
        outputDirectory.set(rootProject.file("docs"))
    }

    val docs = task<Copy>("createDocsIndex") {
        dependsOn(dokkaHtmlMultiModule)
        val outputDirectory = dokkaHtmlMultiModule.get().outputDirectory.get()
        from(outputDirectory)
        include("-modules.html")
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
            }
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
                useIR = true
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
    repoUri.set("https://github.com/DRSchlaubi/furry-octo-rotary-phone.git")
    branch.set("gh-pages")

    contents {
        from(file("docs"))
    }

    commitMessage.set("Update Docs")
}
