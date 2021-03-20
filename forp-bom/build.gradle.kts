plugins {
    `java-platform`
    `maven-publish`
}

group = "dev.schlaubi.forp"
version = "1.0-SNAPSHOT"

dependencies {
    constraints {
        rootProject.subprojects.forEach {
            if (it.plugins.hasPlugin("maven-publish") && it.name != name) {
                it.publishing.publications.all {
                    if (this is MavenPublication) {
                        if (!artifactId.endsWith("-metadata") &&
                            !artifactId.endsWith("-kotlinMultiplatform")) {
                            api(groupId, artifactId, version)
                        }
                    }
                }
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("javaPlatform"))
        }
    }
}
