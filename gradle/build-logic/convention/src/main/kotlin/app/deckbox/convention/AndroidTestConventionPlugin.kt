// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.test")
        apply("org.gradle.android.cache-fix")
      }

      configureAndroid()
    }
  }
}
