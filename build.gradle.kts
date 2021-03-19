import java.util.Base64

plugins {
    kotlin("multiplatform") version "1.4.31" apply false
    id("org.jetbrains.dokka") version "1.4.20"
    id("org.ajoberstar.git-publish") version "2.1.3"
    signing
    `maven-publish`
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
                useIR = true
                freeCompilerArgs = freeCompilerArgs +
                        "-Xopt-in=dev.schlaubi.forp.core.annotation.ForpInternals"
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }

    if (extensions.findByName("publishing") != null) {
        publishing {
            repositories {
                maven {
                    setUrl("https://schlaubi.jfrog.io/artifactory/lavakord")

                    credentials {
                        username = System.getenv("BINTRAY_USER")
                        password = System.getenv("BINTRAY_KEY")
                    }
                }
            }

            publications {
                filterIsInstance<MavenPublication>().forEach { publication ->
                    publication.pom {
                        name.set(project.name)
                        description.set("Kotlin library which can, fetch, find, parse and analyze JVM exception stacktraces")
                        url.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone")

                        licenses {
                            license {
                                name.set("Apache-2.0 License")
                                url.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone/blob/main/LICENSE")
                            }
                        }

                        developers {
                            developer {
                                name.set("Michael Rittmeister")
                                email.set("mail@schlaubi.me")
                                organizationUrl.set("https://michael.rittmeister.in")
                            }
                        }

                        scm {
                            connection.set("scm:git:https://github.com/DRSchlaubi/furry-okto-rotary-phone.git")
                            developerConnection.set("scm:git:https://github.com/DRSchlaubi/furry-okto-rotary-phone.git")
                            url.set("https://github.com/DRSchlaubi/furry-okto-rotary-phone")
                        }
                    }
                }
            }
        }
    }

    if(extensions.findByName("signing") != null) {
        signing {
            val signingKey = findProperty("signingKey")?.toString()
            val signingPassword = findProperty("signingPassword")?.toString()
            if (signingKey != null && signingPassword != null) {
                useInMemoryPgpKeys(String(Base64.getDecoder().decode(signingKey.toByteArray())), signingPassword)
            }

            publishing.publications.withType<MavenPublication> {
                sign(this)
            }
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
