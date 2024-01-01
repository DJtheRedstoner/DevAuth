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
    }
    plugins {
        id("com.github.johnrengelman.shadow") version "7.0.0"
        id("io.github.juuxel.loom-quiltflower") version "1.7.3"
    }
}

include("common")

include("fabric")
include("forge-legacy")
include("forge-latest")
include("neoforge")
