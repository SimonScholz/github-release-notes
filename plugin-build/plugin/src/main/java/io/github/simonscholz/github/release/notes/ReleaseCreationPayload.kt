package io.github.simonscholz.github.release.notes

import com.google.gson.annotations.SerializedName

data class ReleaseCreationPayload(
    @SerializedName("tag_name") val tagName: String,
    val name: String,
    val body: String,
    val draft: Boolean? = true,
)
