plugins {
    kotlin("jvm")
}

group = "board-game"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    testImplementation(kotlin("test"))
    testImplementation(project(":domain"))
    testImplementation(testFixtures(project(":domain")))
    testFixturesImplementation(testFixtures(project(":infra")))
    testFixturesImplementation(testFixtures(project(":domain")))
    testFixturesImplementation(project(":domain"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
