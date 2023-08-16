plugins {
  id("app.deckbox.ui")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.features.decks.public)
        implementation(projects.features.decks.publicUi)
      }
    }
  }
}
