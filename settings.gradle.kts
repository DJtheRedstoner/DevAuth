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
        id("io.github.goooler.shadow") version "8.1.7"
    }
}

include("common")

include("fabric")
include("forge-legacy")
include("forge-latest")
include("neoforge")
