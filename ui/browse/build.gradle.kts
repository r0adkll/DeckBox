plugins {
  id("app.deckbox.ui")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.features.cards.public)
      }
    }
  }
}
