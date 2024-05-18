// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension

class ComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply("org.jetbrains.compose")
    configureCompose()
  }
}

fun Project.configureCompose() {
  with(extensions.getByType<ComposeExtension>()) {
    kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.9.24"))

    kotlinCompilerPluginArgs.addAll(
      // Enable 'strong skipping'
      // https://medium.com/androiddevelopers/jetpack-compose-strong-skipping-mode-explained-cbdb2aa4b900
      "strongSkipping=true",
    )
  }
}
