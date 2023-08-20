import app.deckbox.convention.addKspDependencyForCommon

plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
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
