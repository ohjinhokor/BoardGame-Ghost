plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("plugin.spring") version "1.8.20"
}

group = "board-game.push"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
    developmentOnly("io.netty:netty-resolver-dns-native-macos:4.1.72.Final:osx-aarch_64")

    implementation(project(":domain:core"))
    implementation(project(":domain:game"))
    implementation(project(":domain:player"))
    implementation(project(":domain:escapee"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
