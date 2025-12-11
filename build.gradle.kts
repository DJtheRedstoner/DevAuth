import me.djtheredstoner.devauth.build.fixRemap
import org.gradle.jvm.tasks.Jar

plugins {
    id("java-library")
    id("build-logic")
    id("gg.essential.loom") version "1.6.17" apply false
}

allprojects {
    group = "me.djtheredstoner"
    version = "1.2.1"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"

        options.release.set(21)
    }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }

    tasks.withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "build-logic")

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

fixRemap()