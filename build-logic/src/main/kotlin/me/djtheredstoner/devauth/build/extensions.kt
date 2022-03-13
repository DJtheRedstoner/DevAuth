package me.djtheredstoner.devauth.build

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar

fun Project.configureMcProject() {
    configureCopyArtifacts()
}

fun Project.configureCopyArtifacts() {
    val copyArtifacts = tasks.create( "copyArtifacts", Copy::class.java).apply {
        dependsOn(tasks.getByName("remapJar"))
        from((tasks.getByName("remapJar") as Jar).archiveFile)
        into(rootProject.buildDir.resolve("distributions"))
    }
    tasks.getByName("assemble").dependsOn(copyArtifacts)
}
