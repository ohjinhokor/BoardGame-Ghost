plugins {
    kotlin("jvm")
}

group = "board-game.application"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:core"))
    implementation(project(":domain:game"))
    testImplementation(kotlin("test"))
    testImplementation(project(":domain"))
    testImplementation(testFixtures(project(":domain:core")))
    testFixturesImplementation(testFixtures(project(":infra")))
    testFixturesImplementation(testFixtures(project(":domain:core")))
    testFixturesImplementation(project(":domain:core"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
