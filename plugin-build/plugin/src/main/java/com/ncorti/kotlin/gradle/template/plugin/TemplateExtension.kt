package com.ncorti.kotlin.gradle.template.plugin

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

const val DEFAULT_OUTPUT_FILE = "template-example.txt"

@Suppress("UnnecessaryAbstractClass")
abstract class TemplateExtension @Inject constructor(project: Project) {

    private val objects = project.objects

    val username: Property<String> = objects.property(String::class.java)

    val password: Property<String> = objects.property(String::class.java)

    val owner: Property<String> = objects.property(String::class.java)

    val projectName: Property<String> = objects.property(String::class.java)
}
