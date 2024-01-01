import me.djtheredstoner.devauth.build.configurePublishing
import me.djtheredstoner.devauth.build.configureResources

repositories {
    maven("https://maven.neoforged.net/")
    maven("https://libraries.minecraft.net/")
}

configureResources()

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

val shade: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    compileOnly("net.neoforged:neoforge:20.4.72-beta:universal")

    compileOnly(project(":common"))

    shade(project(":common"))
}

tasks.jar {
    manifest.attributes(
        "MixinConfigs" to "mixins.devauth.json"
    )

    dependsOn(project(":common").tasks.named("jar"))
    from(shade.files.map { zipTree(it) })
}

tasks.sourcesJar {
    from(project(":common").sourceSets.main.get().allSource)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}

configurePublishing()
