plugins {
    kotlin("jvm") version("1.5.31")
    id("java-gradle-plugin")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    gradleApi()
}

gradlePlugin {
    plugins {
        create("build-logic") {
            id = "build-logic"
            implementationClass = "me.djtheredstoner.devauth.build.BuildLogic"
        }
    }
}