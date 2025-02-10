plugins {
    id("java")
}

group = "board-game.application.game"

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
