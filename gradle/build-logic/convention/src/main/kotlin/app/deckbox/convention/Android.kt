// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroid(computeNamespace: Boolean = true) {
  android {
    if (computeNamespace) {
      namespace = "app.deckbox.${path.substring(1).replace(':', '.')}"
    }
    compileSdkVersion(Versions.compileSdk)

    defaultConfig {
      minSdk = Versions.minSdk
      targetSdk = Versions.targetSdk
    }

    // Can remove this once https://issuetracker.google.com/issues/260059413 is fixed.
    // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11

      // https://developer.android.com/studio/write/java8-support
      isCoreLibraryDesugaringEnabled = true
    }
  }

  dependencies {
    // https://developer.android.com/studio/write/java8-support
    "coreLibraryDesugaring"(libs.findLibrary("tools.desugarjdklibs").get())
  }
}

private fun Project.android(action: BaseExtension.() -> Unit) = extensions.configure<BaseExtension>(action)
