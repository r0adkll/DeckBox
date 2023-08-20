// Copyright 2023, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0


plugins {
  `kotlin-dsl`
  alias(libs.plugins.spotless)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

spotless {
  kotlin {
    target("src/**/*.kt")
    ktlint(libs.versions.ktlint.get())
    licenseHeaderFile(rootProject.file("../../spotless/dh-copyright.txt"))
  }

  kotlinGradle {
    target("*.kts")
    ktlint(libs.versions.ktlint.get())
    licenseHeaderFile(rootProject.file("../../spotless/dh-copyright.txt"), "(^(?![\\/ ]\\**).*$)")
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.composeMultiplatform.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.spotless.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("root") {
      id = "app.deckbox.root"
      implementationClass = "app.deckbox.convention.RootConventionPlugin"
    }

    register("ui") {
      id = "app.deckbox.ui"
      implementationClass = "app.deckbox.convention.UiConventionPlugin"
    }

    register("compose") {
      id = "app.deckbox.compose"
      implementationClass = "app.deckbox.convention.ComposeConventionPlugin"
    }

    register("kotlinMultiplatform") {
      id = "app.deckbox.multiplatform"
      implementationClass = "app.deckbox.convention.KotlinMultiplatformConventionPlugin"
    }

    register("kotlinAndroid") {
      id = "app.deckbox.kotlin.android"
      implementationClass = "app.deckbox.convention.KotlinAndroidConventionPlugin"
    }

    register("kotlinJvm") {
      id = "app.deckbox.kotlin.jvm"
      implementationClass = "app.deckbox.convention.KotlinJvmConventionPlugin"
    }

    register("androidApplication") {
      id = "app.deckbox.android.application"
      implementationClass = "app.deckbox.convention.AndroidApplicationConventionPlugin"
    }

    register("androidLibrary") {
      id = "app.deckbox.android.library"
      implementationClass = "app.deckbox.convention.AndroidLibraryConventionPlugin"
    }

    register("androidTest") {
      id = "app.deckbox.android.test"
      implementationClass = "app.deckbox.convention.AndroidTestConventionPlugin"
    }
  }
}
