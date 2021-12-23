package io.github.simonscholz.github.release.notes

import okhttp3.Credentials
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

abstract class PrintGitHubReleaseNotes : DefaultTask() {

    init {
        description = "Prints release notes containing all PRs in the description"
        group = "GitHub release plugin"
    }

    @get:Input
    @get:Option(option = "username", description = "")
    @get:Optional
    abstract val username: Property<String>

    @get:Input
    @get:Option(option = "password", description = "")
    @get:Optional
    abstract val password: Property<String>

    @get:Input
    @get:Option(option = "gitHubToken", description = "")
    @get:Optional
    abstract val gitHubToken: Property<String>

    @get:Input
    @get:Option(option = "baseBranch", description = "")
    @get:Optional
    abstract val baseBranch: Property<String>

    @get:Input
    @get:Option(option = "owner", description = "")
    abstract val owner: Property<String>

    @get:Input
    @get:Option(option = "project", description = "")
    abstract val projectName: Property<String>

    @TaskAction
    fun printGitHubReleaseNotesAction() {
        val auth = if (gitHubToken.isPresent) "Bearer ${gitHubToken.get()}"
        else Credentials.basic(username.get(), password.get())

        val gitHubApi = GitHubApi.create(auth)

        val latestRelease = gitHubApi.getLatestRelease(
            owner.get(),
            projectName.get(),
        ).execute()

        val latestUpdatedPullRequests = gitHubApi.getLatestUpdatedPullRequests(
                owner.get(),
                projectName.get(),
            if (baseBranch.isPresent) baseBranch.get() else "master"
        ).execute().body() ?: mutableListOf()

        logger.lifecycle(latestUpdatedPullRequests.toString() + lineSeparator)

        val latestReleaseBody = latestRelease.body()?.get(0)

        val pullRequests = latestReleaseBody?.publishedAt?.let { published ->
            latestUpdatedPullRequests.filter {
                it.mergedAt?.toInstant()?.isAfter(published.toInstant()) ?: false
            }
        } ?: latestUpdatedPullRequests

        val tagName = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
        val releaseName = "Release $tagName"

        logger.lifecycle(releaseName + lineSeparator)

        val releaseBody = pullRequests.joinToString("") {
            "#${it.number} ${it.title} $lineSeparator"
        }

        logger.lifecycle(releaseBody)
    }
}
