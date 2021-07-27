package io.github.simonscholz.github.release.notes

import org.gradle.api.Project
import org.gradle.api.provider.Property
import javax.inject.Inject

@Suppress("UnnecessaryAbstractClass")
abstract class GitHubReleaseNotesExtension @Inject constructor(project: Project) {

    private val objects = project.objects

    val username: Property<String> = objects.property(String::class.java)

    val password: Property<String> = objects.property(String::class.java)

    val owner: Property<String> = objects.property(String::class.java)

    val projectName: Property<String> = objects.property(String::class.java)
}
