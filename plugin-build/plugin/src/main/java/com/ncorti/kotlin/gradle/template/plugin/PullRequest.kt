package com.ncorti.kotlin.gradle.template.plugin

data class PullRequest(val title: String, val number: Int, val merged_at: String? = null)
