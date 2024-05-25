plugins {
  id("app.deckbox.multiplatform")
  id("app.deckbox.compose")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.core)
        implementation(compose.runtime)
        implementation(compose.ui)
      }
    }
  }
}
