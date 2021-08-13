package io.github.simonscholz.github.release.notes

import java.text.MessageFormat

object Announcement {

    fun createAnnouncement(
        teamName: String,
        releaseName: String,
        projectName: String,
        releaseBody: String,
        releaseUrl: String,
        deploymentAnnouncement: String? = null,
    ) : String =
        deploymentAnnouncement?.let {
            MessageFormat.format(it, teamName, releaseName, projectName, releaseBody, releaseUrl).trimIndent()
        } ?:
        """
:microphone2: :bounce: @here $teamName will deploy our new release $releaseName for our
$projectName service to PROD in a couple of minutes. Changes going live:
```
$releaseBody
```
Feel free to explore all release notes and see the full diff in code here: $releaseUrl
            """.trimIndent()
}
