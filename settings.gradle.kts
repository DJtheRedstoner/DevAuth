rootProject.name = "DevAuth"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://repo.essential.gg/repository/maven-public/")
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.minecraftforge.net/")
    }
}

includeBuild("build-logic")

include("common")

include("fabric")
include("forge-legacy")
include("forge-latest")