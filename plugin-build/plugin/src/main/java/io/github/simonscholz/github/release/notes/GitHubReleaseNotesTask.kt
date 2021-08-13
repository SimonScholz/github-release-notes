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

val lineSeparator: String = System.getProperty("line.separator")

abstract class GitHubReleaseNotesTask : DefaultTask() {

    init {
        description = "Generates a draft release containing all PRs in the description"
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
    @get:Option(option = "deploymentAnnouncement", description = "")
    @get:Optional
    abstract val deploymentAnnouncement: Property<String>

    @get:Input
    @get:Option(option = "teamName", description = "")
    @get:Optional
    abstract val teamName: Property<String>

    @get:Input
    @get:Option(option = "owner", description = "")
    abstract val owner: Property<String>

    @get:Input
    @get:Option(option = "project", description = "")
    abstract val projectName: Property<String>

    @TaskAction
    fun gitHubReleaseNotesAction() {
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
        ).execute()

        logger.lifecycle(latestUpdatedPullRequests.toString())

        val latestReleaseBody = latestRelease.body()?.get(0)

        val pullRequests = latestReleaseBody?.publishedAt?.let { published ->
            latestUpdatedPullRequests.body()?.filter {
                it.mergedAt?.toInstant()?.isAfter(published.toInstant()) ?: false
            }
        } ?: latestUpdatedPullRequests.body()

        val releaseBody = pullRequests?.joinToString("") {
            "#${it.number} ${it.title} $lineSeparator"
        }?.trimMargin()

        val tagName = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
        val releaseName = "Release $tagName"

        val releaseCreationRequestPayload = ReleaseCreationRequestPayload(
            tagName,
            releaseName,
            releaseBody ?: "No Pull Requests"
        )

        logger.lifecycle(releaseCreationRequestPayload.toString())

        val createReleaseExecution = gitHubApi.createRelease(
            owner = owner.get(),
            project = projectName.get(),
            releaseCreationRequestPayload = releaseCreationRequestPayload,
        ).execute()

        logger.lifecycle("$createReleaseExecution")

        val releaseUrl = createReleaseExecution.body()?.url

        val announcement = Announcement.createAnnouncement(
            teamName.getOrElse("ARC"),
            releaseName,
            projectName.get(),
            releaseBody ?: "No PRs for this release",
            releaseUrl ?: "",
            deploymentAnnouncement.orNull
        )

        logger.lifecycle(
            """
$lineSeparator
$announcement
$lineSeparator
              """.trimIndent())
    }
}
