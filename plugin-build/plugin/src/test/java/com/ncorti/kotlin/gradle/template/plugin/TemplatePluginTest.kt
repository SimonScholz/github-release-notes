package com.ncorti.kotlin.gradle.template.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File

class TemplatePluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")

        assert(project.tasks.getByName("templateExample") is TemplateExampleTask)
    }

    @Test
    fun `extension templateExampleConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")

        assertNotNull(project.extensions.getByName("templateExampleConfig"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")
        (project.extensions.getByName("templateExampleConfig") as TemplateExtension).apply {
            username.set("SimonScholz")
            password.set("just-a-secret")
            owner.set("SimonScholz")
            projectName.set("github-release-notes")
        }

        val task = project.tasks.getByName("templateExample") as TemplateExampleTask

        assertEquals("SimonScholz", task.username.get())
        assertEquals("just-a-secret", task.password.get())
        assertEquals("SimonScholz", task.owner.get())
        assertEquals("github-release-notes", task.projectName.get())
    }
}
