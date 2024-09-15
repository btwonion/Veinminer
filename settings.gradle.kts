rootProject.name = "Veinminer"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include(":core")
include(":paper")
include(":veinminer-fabric")
include(":veinminer-enchant-fabric")
