package io.github.simonscholz.github.release.notes

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GitHubReleaseNotesPluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.simonscholz.github.release.notes.plugin")

        assert(project.tasks.getByName(TASK_NAME) is GitHubReleaseNotesTask)
    }

    @Test
    fun `extension gitHubReleaseNotesConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.simonscholz.github.release.notes.plugin")

        assertNotNull(project.extensions.getByName("gitHubReleaseNotesConfig"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.simonscholz.github.release.notes.plugin")
        (project.extensions.getByName("gitHubReleaseNotesConfig") as GitHubReleaseNotesExtension).apply {
            username.set("SimonScholz")
            password.set("just-a-secret")
            owner.set("SimonScholz")
            projectName.set("github-release-notes")
        }

        val task = project.tasks.getByName(TASK_NAME) as GitHubReleaseNotesTask

        assertEquals("SimonScholz", task.username.get())
        assertEquals("just-a-secret", task.password.get())
        assertEquals("SimonScholz", task.owner.get())
        assertEquals("github-release-notes", task.projectName.get())
    }
}
