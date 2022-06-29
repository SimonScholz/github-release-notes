## How to use 👣

### Use it within your own build

```kotlin
plugins {
    id("io.github.simonscholz.github.release.notes.plugin") version "1.5.0"
}

gitHubReleaseNotesConfig {
    project.findProperty("GITHUB_TOKEN")?.let{gitHubToken.set(it.toString())}
    project.findProperty("GITHUB_USER_NAME")?.let{username.set(it.toString())}
    project.findProperty("GITHUB_USER_ACCESS_TOKEN")?.let{password.set(it.toString())}
    owner.set("SimonScholz")
    projectName.set("github-release-notes")
}
```

### Test in within this repo

Adjust the `build.gradle.kts` file in the `example` project
with your username + personal GitHub access token aka password and desired owner + project.

You can run the following gradle task to create a draft release for your desired project:

```
./gradlew example:gitHubReleaseNotesTask
```

In the console you'll also find a proper announcement for the webshop deployment channel which you can copy and paste.

## Gradle Plugin Portal 🚀

The plugin itself can be found here: https://plugins.gradle.org/plugin/io.github.simonscholz.github.release.notes.plugin

## Contributing 🤝

Feel free to open a issue or submit a pull request for any bugs/improvements.

## License 📄

This template is licensed under the MIT License - see the [License](License) file for details.
Please note that the generated template is offering to start with a MIT license but you can change it to whatever you wish, as long as you attribute under the MIT terms that you're using the template. 
