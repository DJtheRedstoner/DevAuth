plugins {
    id("fabric-loom") version "0.9-SNAPSHOT"
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
            runDir(project.file("run").relativeTo(project.rootDir).path)
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.17.1")
    mappings("net.fabricmc:yarn:1.17.1+build.49:v2")
    modImplementation("net.fabricmc:fabric-loader:0.11.6")

    api(project(":common"))

    include(project(":common"))
    include("com.electronwill.night-config:core:3.6.4")
    include("com.electronwill.night-config:toml:3.6.4")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}
