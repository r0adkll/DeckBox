// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import org.gradle.api.Project

fun Project.configureKotlin() {
  // Configure Java to use our chosen language level. Kotlin will automatically pick this up
  configureJava()
}
