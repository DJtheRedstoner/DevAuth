rootProject.name = "DevAuth"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://maven.architectury.dev/")
    }
    plugins {
        id("io.github.goooler.shadow") version "8.1.8"
    }
}

include("common")

include("fabric")
include("neoforge")
