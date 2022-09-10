rootProject.name = "DevAuth"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://repo.essential.gg/repository/maven-public/")
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.legacyfabric.net/")
    }
    plugins {
        id("com.github.johnrengelman.shadow") version "7.0.0"
        id("gg.essential.loom") version "0.10.0.3"
        id("dev.architectury.architectury-pack200") version "0.1.3"
        id("io.github.juuxel.loom-quiltflower") version "1.7.3"
        id("fabric-loom") version "1.0-SNAPSHOT"
    }
}

include("common")

include("fabric")
include("fabric-legacy")
include("forge-legacy")
include("forge-latest")