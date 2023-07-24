package app.deckbox.shared

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
