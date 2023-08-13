@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.1.0")

import com.gianluz.dangerkotlin.androidlint.AndroidLint
import com.gianluz.dangerkotlin.androidlint.androidLint
import systems.danger.kotlin.*

register plugin AndroidLint

danger(args) {

  val allSourceFiles = git.modifiedFiles + git.createdFiles
  val changelogChanged = allSourceFiles.contains("CHANGELOG.md")
  val sourceChanges = allSourceFiles.firstOrNull { it.contains("src") }

  onGitHub {
    val isTrivial = pullRequest.title.contains("#trivial")

    message("This PR has been checked by Danger")

    // Changelog
    if (!isTrivial && !changelogChanged && sourceChanges != null) {
      warn(
        "any changes to library code should be reflected in the Changelog.\n\n" +
          "Please consider adding a note there and adhere to the " +
          "[Changelog Guidelines](https://github.com/Moya/contributors/blob/master/Changelog%20Guidelines.md).",
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

    if (git.linesOfCode > 500) {
      warn("This PR is original Xbox Huge! Consider breaking into smaller PRs")
    }
  }

  androidLint {
    val moduleLintFilesPaths = find(
      projectDir = ".",
      "lint-results-debug.xml",
      "lint-results-release.xml",
    ).toTypedArray()

    reportDistinct(*moduleLintFilesPaths)
  }
}
