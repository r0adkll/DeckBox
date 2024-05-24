plugins {
  id("app.deckbox.ui")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(compose.components.resources)
      }
    }
  }
}
