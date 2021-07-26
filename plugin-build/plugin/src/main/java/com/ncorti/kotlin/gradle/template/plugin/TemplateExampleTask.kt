package com.ncorti.kotlin.gradle.template.plugin

import okhttp3.Callback
import okhttp3.Credentials
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.time.Instant

abstract class TemplateExampleTask : DefaultTask() {

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

        val apiInterface = ApiInterface.create()

        val latestRelease = apiInterface.getLatestRelease(owner.get(), projectName.get(), basic, "application/vnd.github.v3+json").execute()

        val latestUpdatedPullRequests = apiInterface.getLatestUpdatedPullRequests(owner.get(), projectName.get(), basic, "application/vnd.github.v3+json").execute()

        val pullRequests = latestRelease.body()?.get(0)?.published_at.let { published ->
            latestUpdatedPullRequests.body()?.filter {
                it.merged_at?.toInstant()?.isAfter(published?.toInstant()) ?: false
            }
        }?: latestUpdatedPullRequests.body()

        pullRequests?.forEach {
            logger.lifecycle("#${it.number} ${it.title}")
        }
    }
}
