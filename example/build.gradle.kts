plugins {
    java
    id("io.github.simonscholz.github.release.notes.plugin")
}

gitHubReleaseNotesConfig {
    project.findProperty("GITHUB_TOKEN")?.let {
        gitHubToken.set(it.toString())
    }
    project.findProperty("BASE_BRANCH")?.let {
        baseBranch.set(it.toString())
    }
    project.findProperty("GITHUB_USER_NAME")?.let {
        username.set(it.toString())
    }
    project.findProperty("GITHUB_USER_ACCESS_TOKEN")?.let {
        password.set(it.toString())
    }
    owner.set("MediaMarktSaturn")
    projectName.set("mms-customer-delivery-promise")
    deploymentAnnouncement.set(
        """
:egg: @here {0} will deploy our new release {1} to PROD in a couple of minutes. Changes going live:
```
{3}
```
Feel free to explore all release notes and see the full diff in code here: {4}
        """.trimIndent()
    )
    teamName.set("ARC")
}
