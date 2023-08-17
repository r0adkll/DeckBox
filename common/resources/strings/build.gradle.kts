import app.deckbox.convention.addKspDependencyForCommon

plugins {
  alias(libs.plugins.composeMultiplatform)
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  alias(libs.plugins.ksp)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(libs.lyricist)
        api(compose.foundation)
      }
    }
  }
}

addKspDependencyForCommon(libs.lyricist.compiler)
