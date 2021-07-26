package com.ncorti.kotlin.gradle.template.plugin

import java.time.ZonedDateTime
import java.util.*

data class Release(val published_at: Date, val name: String, val draft: Boolean, val url: String)
