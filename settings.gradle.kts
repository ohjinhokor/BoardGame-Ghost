plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "the-great-escape"

include("presentation")
include("application")
include("domain")
include("infra")

//core
include("domain:core")
findProject(":domain:core")?.name = "core"
