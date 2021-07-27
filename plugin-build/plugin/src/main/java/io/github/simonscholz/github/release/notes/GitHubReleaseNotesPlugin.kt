package io.github.simonscholz.github.release.notes

import org.gradle.api.Plugin
import org.gradle.api.Project

const val EXTENSION_NAME = "gitHubReleaseNotesConfig"
const val TASK_NAME = "gitHubReleaseNotesTask"

abstract class GitHubReleaseNotesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Add the 'template' extension object
        val extension = project.extensions.create(EXTENSION_NAME, GitHubReleaseNotesExtension::class.java, project)

        // Add a task that uses configuration from the extension object
        project.tasks.register(TASK_NAME, GitHubReleaseNotesTask::class.java) {
            it.username.set(extension.username)
            it.password.set(extension.password)
            it.owner.set(extension.owner)
            it.projectName.set(extension.projectName)
        }
    }
}
