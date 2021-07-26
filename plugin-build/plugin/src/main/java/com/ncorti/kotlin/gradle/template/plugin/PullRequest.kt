package com.ncorti.kotlin.gradle.template.plugin

import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

data class PullRequest(val title: String, val number: Int, val merged_at: Date? = null)
