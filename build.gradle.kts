plugins {
    kotlin("jvm") version "2.0.0"
}

group = "board-game"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-test-fixtures")

    repositories {
        mavenCentral()
    }

    dependencies {
        val kotestVersion = "5.9.1"
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
