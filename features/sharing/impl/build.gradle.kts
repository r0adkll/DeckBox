import app.deckbox.convention.addKspDependencyForAllTargets

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
  alias(libs.plugins.ksp)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.core)

        api(projects.features.sharing.public)
        api(projects.features.decks.public)

        api(compose.runtime)
        api(compose.ui)
      }
    }

    androidMain {
      dependencies {
        implementation(libs.androidx.core)
      }
    }
  }
}

addKspDependencyForAllTargets(projects.di.kotlinInjectMerge)
