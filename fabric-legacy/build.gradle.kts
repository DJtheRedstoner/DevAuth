import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("fabric-loom")
}

loom {
    intermediaryUrl.set("https://maven.legacyfabric.net/net/legacyfabric/intermediary/%1\$s/intermediary-%1\$s-v2.jar")
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    mixin {
        add(sourceSets.main.get(), "mixins.devauth.refmap.json")
    }
//    setupRemappedVariants.set(false)
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("net.legacyfabric:yarn:1.8.9+build.382:v2")
    modImplementation("net.fabricmc:fabric-loader:0.14.7")

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
