package app.deckbox.common.compose

enum class Platform {
  Android,
  iOS,
  Desktop,
}

expect val currentPlatform: Platform
