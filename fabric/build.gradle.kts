import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("fabric-loom")
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
    minecraft("com.mojang:minecraft:1.21.11")
    mappings(loom.officialMojangMappings())
    modCompileOnly(localRuntime("net.fabricmc:fabric-loader:0.18.2")!!)

    api(project(":common"))

    include(project(":common"))
    include("com.electronwill.night-config:core:3.8.3")
    include("com.electronwill.night-config:toml:3.8.3")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

configureMcProject()
