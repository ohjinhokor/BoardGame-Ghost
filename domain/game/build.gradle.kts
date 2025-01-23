plugins {
    id("java")
}

group = "board-game.domain"
version = "1.0-SNAPSHOT"

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
