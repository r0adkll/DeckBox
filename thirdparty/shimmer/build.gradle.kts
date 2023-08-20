plugins {
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(compose.foundation)
        api(libs.kotlinx.coroutines.core)
      }
    }
  }
}
