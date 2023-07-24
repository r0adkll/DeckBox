@file:Suppress("UnstableApiUsage")

plugins {
  id("app.deckbox.android.application")
  id("app.deckbox.kotlin.android")
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
    kotlinCompilerExtensionVersion = "1.4.8"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
}

dependencies {
  implementation(project(":shared"))

  implementation("androidx.compose.ui:ui:1.4.3")
  implementation("androidx.compose.ui:ui-tooling:1.4.3")
  implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
  implementation("androidx.compose.foundation:foundation:1.4.3")
  implementation("androidx.compose.material:material:1.4.3")
  implementation("androidx.activity:activity-compose:1.7.1")
}
