package app.deckbox.common.compose

enum class Platform {
  ANDROID,
  IOS,
  DESKTOP,
}

expect val currentPlatform: Platform
