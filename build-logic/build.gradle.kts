plugins {
    kotlin("jvm") version("1.6.10")
    id("java-gradle-plugin")
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