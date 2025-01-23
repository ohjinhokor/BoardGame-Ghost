plugins {
    id("java")
}

group = "board-game.domain"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.7.2")
    implementation("com.aallam.ulid:ulid-kotlin:1.3.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}