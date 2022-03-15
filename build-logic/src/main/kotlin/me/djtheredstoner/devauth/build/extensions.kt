package me.djtheredstoner.devauth.build

import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.api.tasks.Copy
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources

fun Project.configureMcProject() {
    configureCopyArtifacts()
    configureResources()
    configurePublishing()
}

fun Project.configureCopyArtifacts() {
    val copyArtifacts = tasks.create( "copyArtifacts", Copy::class.java).apply {
        dependsOn(tasks.getByName("remapJar"))
        from((tasks.getByName("remapJar") as Jar).archiveFile)
        into(rootProject.buildDir.resolve("distributions"))
    }
    tasks.getByName("assemble").dependsOn(copyArtifacts)
}

fun Project.configureResources() {
    tasks.named<ProcessResources>("processResources") {
        from(rootProject.file("LICENSE")) {
            rename { "LICENSE_DevAuth.txt" }
        }

        from(rootProject.file("branding/logo128x.png")) {
            rename { "assets/devauth/logo.png" }
        }

        duplicatesStrategy = DuplicatesStrategy.WARN
    }
}

// please spare me
fun Project.configurePublishing() {
    configure<PublishingExtension> {
        publications.apply {
            create("maven", MavenPublication::class.java).apply {
                artifactId = "DevAuth-${project.name}"

                from(components.getByName("java"))

                if (project.name == "forge-latest") {
                    pom.withXml {
                        (asNode()["dependencies"] as NodeList).forEach {
                            (it as Node).parent().remove(it)
                        }
                    }
                }
            }
        }

        if (project.hasProperty("devAuthMavenUser")) {
            repositories {
                maven {
                    url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
                    name = "DevAuth-public"
                    authentication {
                        create<BasicAuthentication>("basic")
                    }
                    credentials {
                        username = project.property("devAuthMavenUser") as String
                        password = project.property("devAuthMavenPass") as String
                    }
                }
            }
        }
    }

    afterEvaluate {
        val jarTask = if (project.name == "forge-latest") "shadowJar" else "jar"
        configurations.all {
            if (artifacts.removeIf { it.classifier == "thin" }) {
                project.artifacts.add(name, tasks.named(jarTask)) {
                    classifier = null
                }
            }

            if (artifacts.removeIf { it.classifier == "sources-dev" }) {
                project.artifacts.add(name, tasks.named("sourcesJar")) {
                    classifier = "source"
                }
            }

            artifacts.removeIf { it.classifier == "fat" }
        }
    }

    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }
}

fun Project.fixRemap() {
//        subprojects.mapNotNull { it.tasks. }.reduce { t1, t2 ->
//            t2.mustRunAfter(t1)
//            t2
//        }
//    }
    gradle.projectsEvaluated {
        subprojects.mapNotNull { it.tasks.findByPath("remapJar") }.reduce { t1, t2 ->
            t2.mustRunAfter(t1)
            t2
        }
    }
}