plugins {
    id("java")
}

group = "board-game.application.game"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:core"))
    implementation(project(":domain:game"))
    testImplementation(testFixtures(project(":domain:core")))
    testImplementation(testFixtures(project(":domain:game")))
}

tasks.test {
    useJUnitPlatform()
}
