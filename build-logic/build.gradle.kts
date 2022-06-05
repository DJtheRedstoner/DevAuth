plugins {
    kotlin("jvm") version("1.5.31")
    `kotlin-dsl`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

repositories {
    mavenCentral()
}

dependencies {
    gradleApi()
}
