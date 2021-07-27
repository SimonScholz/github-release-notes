plugins {
    java
    id("io.github.simonscholz.github.release.notes.plugin")
}

gitHubReleaseNotesConfig {
    username.set("SimonScholz")
    password.set("")
    owner.set("MediaMarktSaturn")
    projectName.set("mms-customer-delivery-promise")
}
