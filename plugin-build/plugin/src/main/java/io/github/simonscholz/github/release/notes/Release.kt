package io.github.simonscholz.github.release.notes

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Release(
    @SerializedName("published_at") val publishedAt: Date,
    val name: String,
    val draft: Boolean,
    val url: String,
)
