plugins {
    id("java")
}

group = "board-game.domain.escapee"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:core"))
    implementation(project(":domain:game"))
    implementation(project(":domain:player"))

    testImplementation(testFixtures(project(":domain:escapee")))
    testImplementation(testFixtures(project(":domain:player")))

    testFixturesImplementation(testFixtures(project(":domain:core")))
}
