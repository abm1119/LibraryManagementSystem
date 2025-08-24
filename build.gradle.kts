plugins {
    kotlin("jvm") version "2.2.0"    // use a stable release instead of 2.2.0
    application
}

group = "com.abdulbasit"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        url = uri("https://repo1.maven.org/maven2")
    }
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}



