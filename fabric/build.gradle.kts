import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("fabric-loom") version "1.1-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower")
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    mixin {
        add(sourceSets.main.get(), "mixins.devauth.refmap.json")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.2:v2")
    modCompileOnly("net.fabricmc:fabric-loader:0.13.3")

    api(project(":common"))

    include(project(":common"))
    include("com.electronwill.night-config:core:3.6.5")
    include("com.electronwill.night-config:toml:3.6.5")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

configureMcProject()
