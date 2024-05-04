plugins {
  id("app.deckbox.ui")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.features.expansions.public)
        implementation(projects.features.cards.public)
        implementation(projects.features.collection.public)
        implementation(projects.ui.filter)
      }
    }
  }
}
