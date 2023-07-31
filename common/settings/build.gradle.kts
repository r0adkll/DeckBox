import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.ksp)
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.core)
        implementation(projects.features.expansions.public)
        api(libs.multiplatformsettings.core)
        api(libs.multiplatformsettings.coroutines)
      }
    }

    val androidMain by getting {
      dependencies {
        implementation(libs.androidx.preferences)
      }
    }
  }
}

addKspDependencyForCommon(projects.di.kotlinInjectMerge)
