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
            property("fml.coreMods.load", "me.djtheredstoner.devauth.forge.legacy.DevAuthLoadingPlugin")
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
    minecraft("com.mojang:minecraft:1.12.2")
    mappings("de.oceanlabs.mcp:mcp_stable:39-1.12")
    forge("net.minecraftforge:forge:1.12.2-14.23.0.2486")

    shade(project(":common"))
    shade("com.electronwill.night-config:core:3.6.5")
    shade("com.electronwill.night-config:toml:3.6.5")
    shade("com.google.code.gson:gson:2.4")
}

tasks {
    jar {
        archiveClassifier.set("thin")
    }
    shadowJar {
        archiveClassifier.set("fat")
        configurations = listOf(shade)
        relocate("com.google.gson", "me.djtheredstoner.devauth.gson")
    }
    remapJar {
        input.set(shadowJar.get().archiveFile)
    }
}

configureMcProject()
