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
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
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
            rename {
                if (project.name == "forge-latest") {
                    "logo.png"
                } else {
                    "assets/devauth/logo.png"
                }
            }
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
        if (project.name == "forge-legacy") {
            configurations.all {
                artifacts.removeIf { it.classifier != "" && it.classifier != "sources" }
            }
        }
    }
}

fun Project.fixRemap() {
    gradle.projectsEvaluated {
        subprojects.mapNotNull { it.tasks.findByPath("remapJar") }.reduce { t1, t2 ->
            t2.mustRunAfter(t1)
            t2
        }
    }
}