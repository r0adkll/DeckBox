@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.1.0")

import com.gianluz.dangerkotlin.androidlint.AndroidLint
import com.gianluz.dangerkotlin.androidlint.androidLint
import systems.danger.kotlin.*

register plugin AndroidLint

danger(args) {

  androidLint {
    report("androidApp/build/reports/lint-results-release.xml")
  }
}
