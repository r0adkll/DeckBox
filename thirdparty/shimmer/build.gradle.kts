plugins {
  alias(libs.plugins.composeMultiplatform)
  id("app.deckbox.android.library")
  id("app.deckbox.multiplatform")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(compose.foundation)
        api(libs.kotlinx.coroutines.core)
      }
    }
  }
}
