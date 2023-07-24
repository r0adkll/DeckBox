package app.deckbox.shared

class AndroidPlatform : Platform {
  override val name: String = "Desktop"
}

actual fun getPlatform(): Platform = AndroidPlatform()
