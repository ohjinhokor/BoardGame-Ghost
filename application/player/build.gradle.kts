plugins {
    id("java")
}

group = "board-game.application.player"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:core"))
    implementation(project(":domain:game"))
    implementation(project(":domain:player"))
    implementation(project(":application:game"))

    testImplementation(testFixtures(project(":domain:core")))
    testImplementation(testFixtures(project(":domain:player")))
    testImplementation(testFixtures(project(":domain:game")))
}
