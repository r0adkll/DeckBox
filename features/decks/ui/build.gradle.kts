plugins {
  id("app.deckbox.ui")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.features.decks.public)
        implementation(projects.features.decks.publicUi)
      }
    }
  }
}
