plugins {
    id("java")
}

group = "board-game.domain.player"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:core"))
    implementation(project(":domain:game"))

    testImplementation(testFixtures(project(":domain:player")))
    testImplementation(testFixtures(project(":domain:game")))

    testFixturesImplementation(testFixtures(project(":domain:core")))
}

tasks.test {
    useJUnitPlatform()
}
