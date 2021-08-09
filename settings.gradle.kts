pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = ("github-release-notes")

include(":example")
includeBuild("plugin-build")
