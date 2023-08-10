import systems.danger.kotlin.*

danger(args) {

  val allSourceFiles = git.modifiedFiles + git.createdFiles
  val changelogChanged = allSourceFiles.contains("CHANGELOG.md")
  val sourceChanges = allSourceFiles.firstOrNull { it.contains("src") }

  onGitHub {
    val isTrivial = pullRequest.title.contains("#trivial")

    // Changelog
    if (!isTrivial && !changelogChanged && sourceChanges != null) {
      warn(
        WordUtils.capitalize(
          "any changes to library code should be reflected in the Changelog.\n\n" +
            "Please consider adding a note there and adhere to the " +
            "[Changelog Guidelines](https://github.com/Moya/contributors/blob/master/Changelog%20Guidelines.md).",
        ),
      )
    }

    // Big PR Check
    if ((pullRequest.additions ?: 0) - (pullRequest.deletions ?: 0) > 300) {
      warn("Big PR, try to keep changes smaller if you can")
    }

    // Work in progress check
    if (pullRequest.title.contains("WIP", false)) {
      warn("PR is classed as Work in Progress")
    }
  }
}
