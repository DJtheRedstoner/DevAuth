import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("dev.architectury.loom") version "0.12.0-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower")
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
}

val shade: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:1.18.2-40.0.13")

    api(project(":common"))

    shade(project(":common"))
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    jar {
        dependsOn(project(":common").tasks.named("jar"))
        from(shade.files.map { zipTree(it) })
    }
}

configureMcProject()
