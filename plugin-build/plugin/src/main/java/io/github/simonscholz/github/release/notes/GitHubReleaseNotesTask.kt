package io.github.simonscholz.github.release.notes

import okhttp3.Credentials
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val lineSeparator = System.getProperty("line.separator")

abstract class GitHubReleaseNotesTask : DefaultTask() {

    init {
        description = "Just a sample template task"

        // Don't forget to set the group here.
        // group = BasePlugin.BUILD_GROUP
    }

    @get:Input
    @get:Option(option = "username", description = "")
    abstract val username: Property<String>

    @get:Input
    @get:Option(option = "password", description = "")
    abstract val password: Property<String>

    @get:Input
    @get:Option(option = "owner", description = "")
    abstract val owner: Property<String>

    @get:Input
    @get:Option(option = "project", description = "")
    abstract val projectName: Property<String>

    @TaskAction
    fun sampleAction() {
        val basic = Credentials.basic(username.get(), password.get())

        val gitHubApi = GitHubApi.create()

        val latestRelease = gitHubApi.getLatestRelease(
            owner.get(),
            projectName.get(),
            basic,
            "application/vnd.github.v3+json"
        ).execute()

        val latestUpdatedPullRequests = gitHubApi.getLatestUpdatedPullRequests(
            owner.get(),
            projectName.get(),
            basic,
            "application/vnd.github.v3+json"
        ).execute()

        val latestReleaseBody = latestRelease.body()?.get(0)

        val pullRequests = latestReleaseBody?.publishedAt?.let { published ->
            latestUpdatedPullRequests.body()?.filter {
                it.mergedAt?.toInstant()?.isAfter(published.toInstant()) ?: false
            }
        } ?: latestUpdatedPullRequests.body()

        val releaseBody = pullRequests?.joinToString("") {
            "#${it.number} ${it.title} $lineSeparator"
        }

        val tagName = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
        val releaseName = "Release $tagName"

        val releaseCreationPayload = ReleaseCreationPayload(tagName, releaseName, releaseBody ?: "No Pull Requests to release")

        logger.lifecycle(releaseCreationPayload.toString())

        val createReleaseExecution = gitHubApi.createRelease(
            owner = owner.get(),
            project = projectName.get(),
            authorization = basic,
            accept = "application/vnd.github.v3+json",
            releaseCreationPayload = releaseCreationPayload,
        ).execute()

        logger.lifecycle("$createReleaseExecution")
    }
}
