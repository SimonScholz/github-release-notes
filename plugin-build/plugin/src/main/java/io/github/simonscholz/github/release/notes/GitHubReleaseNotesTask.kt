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

val lineSeparator: String = System.getProperty("line.separator")

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
        val basicAuth = Credentials.basic(username.get(), password.get())

        val gitHubApi = GitHubApi.create(basicAuth)

        val latestRelease = gitHubApi.getLatestRelease(
            owner.get(),
            projectName.get(),
        ).execute()

        val latestUpdatedPullRequests = gitHubApi.getLatestUpdatedPullRequests(
            owner.get(),
            projectName.get(),
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

        val releaseCreationRequestPayload = ReleaseCreationRequestPayload(tagName, releaseName, releaseBody ?: "No Pull Requests")

        logger.lifecycle(releaseCreationRequestPayload.toString())

        val createReleaseExecution = gitHubApi.createRelease(
            owner = owner.get(),
            project = projectName.get(),
            releaseCreationRequestPayload = releaseCreationRequestPayload,
        ).execute()

        logger.lifecycle("$createReleaseExecution")

        logger.lifecycle("""
            :microphone2: :bounce: @here ARC will deploy our new release $releaseName for our
            ${projectName.get()} service to PROD in a couple of minutes. Changes going live:
                ```
                $releaseBody
                ```
            Feel free to explore all release notes and see the full diff in code here: ${createReleaseExecution.body()?.url}
        """.trimIndent())
    }
}
