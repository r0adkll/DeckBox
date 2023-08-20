// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class UiConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    // Apply other conventions
    with(pluginManager) {
      apply("app.deckbox.android.library")
      apply("app.deckbox.multiplatform")
      apply("app.deckbox.compose")
      libs.findPlugin("ksp").ifPresent { apply(it.get().pluginId) }
    }

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets["commonMain"].dependencies {
        /*
         * This brings in the necessary transitive dependencies for building screens
         * and ui. It contains mostly compose-based deps as well as a few common/core project
         * modules.
         */
        implementation(project(":common:compose"))

        val compose = ComposePlugin.Dependencies(project)
        implementation(compose.runtime)
        implementation(compose.ui)

        libs.findLibrary("circuit-runtime").ifPresent { implementation(it) }
      }
      sourceSets["commonTest"].dependencies {
        libs.findLibrary("kotlin-test").ifPresent { implementation(it) }
      }
    }

    addKspDependencyForCommon(project(":di:kotlin-inject-merge"))
  }
}
