rootProject.name = "DevAuth"

pluginManagement {
    repositories {
        mavenCentral()
        maven(url = "https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}

include("common")

include("fabric")
include("forge-legacy")
