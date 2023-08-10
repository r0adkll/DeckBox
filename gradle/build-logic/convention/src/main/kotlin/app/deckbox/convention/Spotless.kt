// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureSpotless() {
  if (path.startsWith(":thirdparty")) {
    println("Skipping Spotless")
    return
  }

  val ktlintVersion = libs.findVersion("ktlint").get().requiredVersion

  with(pluginManager) {
    apply("com.diffplug.spotless")
  }

  spotless {
    kotlin {
      target("src/**/*.kt")
      targetExclude("src/**/impl/**/*PagingSource.kt")
      ktlint(ktlintVersion)
      licenseHeaderFile(rootProject.file("spotless/google-copyright.txt"))
        .named("google")
        .onlyIfContentMatches("Copyright \\d+,* Google")
      licenseHeaderFile(rootProject.file("spotless/dh-copyright.txt"))
        .named("dh-existing")
        .onlyIfContentMatches("Copyright \\d+,* Drew Heavner")
      licenseHeaderFile(rootProject.file("spotless/dh-copyright.txt"))
        .named("dh-none")
        .onlyIfContentMatches("^(?!// Copyright).*\$")
    }

    kotlinGradle {
      target("*.kts")
      ktlint(ktlintVersion)
      licenseHeaderFile(rootProject.file("spotless/google-copyright.txt"), "(^(?![\\/ ]\\**).*$)")
        .named("google")
        .onlyIfContentMatches("Copyright \\d+,* Google")
      licenseHeaderFile(rootProject.file("spotless/dh-copyright.txt"), "(^(?![\\/ ]\\**).*$)")
        .named("dh-existing")
        .onlyIfContentMatches("Copyright \\d+,* Drew Heavner")
      licenseHeaderFile(rootProject.file("spotless/dh-copyright.txt"), "(^(?![\\/ ]\\**).*$)")
        .named("dh-none")
        .onlyIfContentMatches("^(?!// Copyright).*\$")
    }
  }
}

private fun Project.spotless(action: SpotlessExtension.() -> Unit) = extensions.configure<SpotlessExtension>(action)
