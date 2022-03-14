import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("gg.essential.loom") version "0.10.0.1"
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
    setupRemappedVariants.set(false)
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.2:v2")
    modImplementation("net.fabricmc:fabric-loader:0.13.3")

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
