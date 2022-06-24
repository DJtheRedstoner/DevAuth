import dev.architectury.pack200.java.Pack200Adapter
import me.djtheredstoner.devauth.build.configureMcProject

plugins {
    id("gg.essential.loom")
    id("io.github.juuxel.loom-quiltflower")
    id("dev.architectury.architectury-pack200")
    id("com.github.johnrengelman.shadow")
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
            property("fml.coreMods.load", "me.djtheredstoner.devauth.forge.legacy.DevAuthLoadingPlugin")
        }
    }
    forge {
        pack200Provider.set(Pack200Adapter())
    }
    setupRemappedVariants.set(false)
}

val shade: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    minecraft("com.mojang:minecraft:1.12.2")
    mappings("de.oceanlabs.mcp:mcp_stable:39-1.12")
    forge("net.minecraftforge:forge:1.12.2-14.23.0.2486")

    api(project(":common"))

    shade(project(":common"))
    shade("com.electronwill.night-config:core:3.6.5")
    shade("com.electronwill.night-config:toml:3.6.5")
}

tasks {
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
