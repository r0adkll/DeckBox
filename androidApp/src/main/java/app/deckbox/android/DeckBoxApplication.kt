package app.deckbox.android

import android.app.Application
import app.deckbox.android.di.AndroidAppComponent
import app.deckbox.android.di.create
import app.deckbox.core.extensions.unsafeLazy

class DeckBoxApplication : Application() {

  val component: AndroidAppComponent by unsafeLazy {
    AndroidAppComponent.create(this)
  }

  override fun onCreate() {
    super.onCreate()
  }
}
