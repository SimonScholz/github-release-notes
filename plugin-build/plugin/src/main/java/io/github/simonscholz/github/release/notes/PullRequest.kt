package io.github.simonscholz.github.release.notes

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PullRequest(
    val title: String,
    val number: Int,
    @SerializedName("merged_at") val mergedAt: Date? = null,
)
