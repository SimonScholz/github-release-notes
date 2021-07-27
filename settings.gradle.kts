pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = ("github-release-notes")

include(":example")
includeBuild("plugin-build")
