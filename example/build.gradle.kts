plugins {
    java
    id("io.github.simonscholz.github.release.notes.plugin")
}

gitHubReleaseNotesConfig {
    project.findProperty("GITHUB_TOKEN")?.let{gitHubToken.set(it.toString())}
    project.findProperty("GITHUB_USER_NAME")?.let{username.set(it.toString())}
    project.findProperty("GITHUB_USER_ACCESS_TOKEN")?.let{password.set(it.toString())}
    owner.set("MediaMarktSaturn")
    projectName.set("mms-customer-delivery-promise")
}
