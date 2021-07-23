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

abstract class TemplateExampleTask : DefaultTask() {

    init {
        description = "Just a sample template task"

        // Don't forget to set the group here.
        // group = BasePlugin.BUILD_GROUP
    }

    @get:Input
    @get:Option(option = "message", description = "A message to be printed in the output file")
    abstract val message: Property<String>

    @get:Input
    @get:Option(option = "tag", description = "A Tag to be used for debug and in the output file")
    @get:Optional
    abstract val tag: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun sampleAction() {
        val prettyTag = tag.orNull?.let { "[$it]" } ?: ""

        logger.lifecycle("$prettyTag message is: ${message.orNull}")
        logger.lifecycle("$prettyTag tag is: ${tag.orNull}")
        logger.lifecycle("$prettyTag outputFile is: ${outputFile.orNull}")

        outputFile.get().asFile.writeText("$prettyTag ${message.get()}")

        val basic = Credentials.basic("", "")

        val apiInterface = ApiInterface.create()

        val latestRelease = apiInterface.getLatestRelease("MediaMarktSaturn", "mms-customer-delivery-promise", basic, "application/vnd.github.v3+json").execute()

        val latestUpdatedPullRequests = apiInterface.getLatestUpdatedPullRequests("MediaMarktSaturn", "mms-customer-delivery-promise", basic, "application/vnd.github.v3+json").execute()

        logger.lifecycle("$prettyTag latestRelease is: $latestRelease")
        latestRelease.body()?.forEach {
            logger.lifecycle("$prettyTag Release is: $it")
        }

        logger.lifecycle("$prettyTag latestUpdatedPullRequests is: $latestUpdatedPullRequests")
        latestUpdatedPullRequests.body()?.forEach {
            logger.lifecycle("$prettyTag PullRequest is: $it")
        }
    }
}
