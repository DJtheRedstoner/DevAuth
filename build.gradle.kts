import org.gradle.jvm.tasks.Jar

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
}

allprojects {
    group = "me.djtheredstoner"
    version = "0.2"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"

        options.encoding = "UTF-8"
    }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")

        from(rootProject.projectDir.resolve("LICENSE")) {
            rename { "LICENSE_DevAuth.txt" }
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        println("${project.name} ${archiveClassifier.get()}")
    }
}

subprojects {
    apply(plugin = "java-library")
}