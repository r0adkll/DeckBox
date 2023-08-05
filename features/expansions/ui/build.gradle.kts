plugins {
  id("app.deckbox.ui")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.features.expansions.public)
        implementation(projects.features.cards.public)
      }
    }
  }
}
