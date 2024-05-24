plugins {
  id("app.deckbox.ui")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.features.cards.public)
        implementation(projects.features.decks.public)
        implementation(projects.features.decks.publicUi)
        implementation(projects.features.tournament.public)
        implementation(projects.features.sharing.public)
      }
    }
  }
}
