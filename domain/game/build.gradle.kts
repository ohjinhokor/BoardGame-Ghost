plugins {
    id("java")
}

group = "board-game.domain.game"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:core"))
    testImplementation(testFixtures(project(":domain:core")))
    testFixturesImplementation(testFixtures(project(":domain:core")))
}

tasks.test {
    useJUnitPlatform()
}
