import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("gg.essential.loom")
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

    compileOnly(project(":common"))

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

    sourcesJar {
        from(project(":common").sourceSets.main.get().allSource)
    }
}

configureMcProject()
