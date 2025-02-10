plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "the-great-escape"

include("presentation")
include("infra")
include("push")

include("domain")
include("domain:game")
findProject(":domain:game")?.name = "game"
include("domain:core")
findProject(":domain:core")?.name = "core"
include("domain:player")
findProject(":domain:player")?.name = "player"
include("domain:escapee")
findProject(":domain:escapee")?.name = "escapee"

include("application")
include("application:game")
findProject(":application:game")?.name = "game"
include("application:player")
findProject(":application:player")?.name = "player"
