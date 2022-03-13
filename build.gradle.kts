import org.gradle.jvm.tasks.Jar

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("build-logic")
}

allprojects {
    group = "me.djtheredstoner"
    version = "0.3"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"

        options.encoding = "UTF-8"

        options.release.set(8)
    }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "build-logic")
}