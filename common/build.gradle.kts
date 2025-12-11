import me.djtheredstoner.devauth.build.configurePublishing

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // Not provided by minecraft
    api("com.electronwill.night-config:toml:3.8.3")

    // Provided by minecraft 1.21.11
    compileOnly("com.mojang:authlib:7.0.61")
    compileOnly("org.apache.logging.log4j:log4j-core:2.25.2")
    compileOnly("com.google.code.gson:gson:2.13.2")
    compileOnly("commons-io:commons-io:2.20.0")
    compileOnly("commons-codec:commons-codec:1.19.0")
}

configurePublishing()
