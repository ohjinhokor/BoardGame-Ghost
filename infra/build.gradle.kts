plugins {
    kotlin("jvm")
    id("nu.studer.jooq") version "9.0"
}

group = "board-game.infra"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.1")
    implementation("org.jooq:jooq:3.19.18")
    implementation("org.jooq:jooq-kotlin:3.19.18")
    implementation("org.jooq:jooq-kotlin-coroutines:3.19.18")
    jooqGenerator("org.mariadb.jdbc:mariadb-java-client:3.5.1")

    testFixturesImplementation(project(":domain"))
}

val databaseEngine = providers.environmentVariable("DATABASE_ENGINE").getOrElse("mariadb")
val databaseHost = providers.environmentVariable("DATABASE_HOST").getOrElse("localhost")
val databasePort = providers.environmentVariable("DATABASE_PORT").getOrElse("3307")
val databaseUrl = "jdbc:$databaseEngine://$databaseHost:$databasePort"
val databaseUser = providers.environmentVariable("DATABASE_USERNAME").getOrElse("")
val databasePassword = providers.environmentVariable("DATABASE_PASSWORD").getOrElse("")
val databaseSchema = providers.environmentVariable("DATABASE_SCHEMA").getOrElse("")
val migrationTable = "flyway_schema_history"

jooq {
    configurations {
        create("main") {
            // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.mariadb.jdbc.Driver"
                    url = databaseUrl
                    user = databaseUser
                    password = databasePassword
                    properties =
                        listOf(
                            org.jooq.meta.jaxb.Property().apply {
                                key = "PAGE_SIZE"
                                value = "2048"
                            },
                        )
                }

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.jdbc.JDBCDatabase"
                        inputSchema = databaseSchema
                        includes = ".*"
                        excludes = "$migrationTable|deleted_at"
                        isIncludeExcludeColumns = true
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = true
                        isKotlinNotNullRecordAttributes = true
                        isKotlinNotNullPojoAttributes = true
                        isKotlinNotNullInterfaceAttributes = true
                        isKotlinDefaultedNullablePojoAttributes = false
                        isKotlinDefaultedNullableRecordAttributes = false
                    }
                    target.apply {
                        packageName = "com.jooq"
                        directory = "src/generated/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks {
    named("generateJooq").configure {}
}
kotlin {
    jvmToolchain(21)
}
