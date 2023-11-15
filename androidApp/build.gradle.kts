@file:Suppress("UnstableApiUsage")

plugins {
  id("app.deckbox.android.application")
  id("app.deckbox.kotlin.android")
  alias(libs.plugins.ksp)
}

ksp {
  arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

android {
  namespace = "app.deckbox.android"

  defaultConfig {
    applicationId = "app.deckbox.android"
    versionCode = 1
    versionName = "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.4"
  }

  packaging {
    resources.excludes += setOf(
      // Exclude AndroidX version files
      "META-INF/*.version",
      // Exclude consumer proguard files
      "META-INF/proguard/*",
      // Exclude the Firebase/Fabric/other random properties files
      "/*.properties",
      "fabric/*.properties",
      "META-INF/*.properties",
      // License files
      "LICENSE*",
      // Exclude Kotlin unused files
      "META-INF/**/previous-compilation-data.bin",
    )
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
}

dependencies {
  implementation(projects.shared)
  implementation(projects.common.screens)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.browser)

  implementation(libs.circuit.runtime)
  implementation(libs.circuit.foundation)

  ksp(projects.di.kotlinInjectMerge)
  ksp(libs.kotlininject.ksp)
}
