package io.github.simonscholz.github.release.notes

import org.junit.Assert.assertEquals
import org.junit.Test

internal class AnnouncementTest {

    @Test
    fun `create default announcement`() {
        // given
        val expected =
        """
:microphone2: :bounce: @here ARC will deploy our new release 1234 for our
CDP service to PROD in a couple of minutes. Changes going live:
```
#1 First PR
#2 Second PR
```
Feel free to explore all release notes and see the full diff in code here: https://nohello.net/
            """.trimIndent()

        // when
        val createAnnouncement = Announcement.createAnnouncement(
            "ARC",
            "1234",
            "CDP",
                """
                #1 First PR
                #2 Second PR
                """.trimIndent(),
            "https://nohello.net/",
        )

        // then
        assertEquals(expected, createAnnouncement)
    }

    @Test
    fun `create custom announcement`() {
        // given
        val expected =
            """
:egg: @here XCC will deploy our new release 1234 for our
XCCB service to PROD in a couple of minutes. Changes going live:
```
#1 First PR
#2 Second PR
```
Feel free to explore all release notes and see the full diff in code here: https://nohello.net/
            """.trimIndent()

        val deploymentAnnouncement =
            """
:egg: @here {0} will deploy our new release {1} for our
{2} service to PROD in a couple of minutes. Changes going live:
```
{3}
```
Feel free to explore all release notes and see the full diff in code here: {4}
            """.trimIndent()

        // when
        val createAnnouncement = Announcement.createAnnouncement(
            "XCC",
            "1234",
            "XCCB",
            """
                #1 First PR
                #2 Second PR
                """.trimIndent(),
            "https://nohello.net/",
            deploymentAnnouncement,
        )

        // then
        assertEquals(expected, createAnnouncement)
    }
}
