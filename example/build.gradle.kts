plugins {
    java
    id("io.github.simonscholz.github.release.notes.plugin")
}

gitHubReleaseNotesConfig {
    username.set("SimonScholz")
    password.set(
        project.property("GITHUB_ACCESS_TOKEN")?.toString()
            ?: throw GradleException("GITHUB_ACCESS_TOKEN is missing in your ~/home/.gradle/gradle.properties")
    )
    owner.set("MediaMarktSaturn")
    projectName.set("mms-customer-delivery-promise")
}
