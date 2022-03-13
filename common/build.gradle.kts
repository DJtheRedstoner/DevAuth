repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // Not provided by minecraft
    api("com.electronwill.night-config:toml:3.6.5")

    // Provided by minecraft
    implementation("com.mojang:authlib:1.5.25")                     // 1.18.2: 3.3.39  1.12.2: 1.5.25  1.8.9: 1.5.21
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")    // 1.18.2: 2.17.0  1.12.2: 2.8.1   1.8.9: 2.0-beta9
    implementation("com.google.code.gson:gson:2.8.9")               // 1.18.2: 2.8.9   1.12.2: 2.8.0   1.8.9: 2.2.4
    implementation("commons-io:commons-io:2.11.0")                  // 1.18.2: 2.11.0  1.12.2: 2.5     1.8.9: 2.4
    implementation("commons-codec:commons-codec:1.15")              // 1.18.2: 1.15    1.12.2: 1.10    1.8.9: 1.9
    implementation("org.apache.httpcomponents:httpclient:4.5.13")   // 1.18.2: 4.5.13  1.12.2: 4.3.3   1.8.9: 4.3.3
}

tasks.processResources {
    from(rootProject.file("LICENSE")) {
        rename { "LICENSE_DevAuth.txt" }
    }

    from(rootProject.file("branding/logo128x.png")) {
        rename { "assets/devauth/logo.png" }
    }
}