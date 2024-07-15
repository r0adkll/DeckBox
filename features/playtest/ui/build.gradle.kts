plugins {
  id("app.deckbox.ui")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.features.cards.public)
        implementation(projects.features.decks.public)
        implementation(projects.ui.filter)

        implementation(compose.components.resources)
      }
    }
  }
}
