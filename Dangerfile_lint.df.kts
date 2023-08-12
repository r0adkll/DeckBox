@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.1.0")

import com.gianluz.dangerkotlin.androidlint.AndroidLint
import com.gianluz.dangerkotlin.androidlint.androidLint
import systems.danger.kotlin.*

register plugin AndroidLint

danger(args) {

  androidLint {
    val moduleLintFilesPaths = find(
      projectDir = ".",
      "lint-results-debug.xml",
      "lint-results-release.xml",
    ).toTypedArray()

    reportDistinct(*moduleLintFilesPaths)

//    parseAllDistinct(*moduleLintFilesPaths).forEach {
//      if (it.severity == "Fatal") {
//        fail(
//          "Danger lint check failed: ${it.message}",
//          it.location.file.replace(System.getProperty("user.dir"), ""),
//          it.location.line.toInt(),
//        )
//      }
//    }
  }
}
