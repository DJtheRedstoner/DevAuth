import dev.architectury.pack200.java.Pack200Adapter
import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("gg.essential.loom") version "0.10.0.1"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow")
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    forge {
        pack200Provider.set(Pack200Adapter())
    }
}

val shade: Configuration by configurations.creating {
    isTransitive = false
}
configurations.implementation.get().extendsFrom(shade)

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:1.18.2-40.0.13")

    shade(project(":common"))
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }

        /*from(rootProject.file("LICENSE")) {
            rename { "LICENSE_DevAuth.txt" }
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        from(rootProject.file("branding/logo128x.png")) {
            rename { "assets/devauth/logo.png" }
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }*/
    }

    jar {
        archiveClassifier.set("thin")

        manifest.attributes(mapOf(
            "FMLCorePlugin" to "me.djtheredstoner.devauth.forge.legacy.DevAuthLoadingPlugin"
        ))
    }
    shadowJar {
        archiveClassifier.set("fat")
        configurations = listOf(shade)
    }
    remapJar {
        input.set(shadowJar.get().archiveFile)
    }
}

configureMcProject()
