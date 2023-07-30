package app.deckbox.android

import android.app.Application
import app.deckbox.android.di.AndroidAppComponent
import app.deckbox.android.logging.AndroidBark
import app.deckbox.core.extensions.unsafeLazy
import app.deckbox.core.logging.Heartwood
import kotlininject.merge.app.deckbox.android.di.MergedAndroidAppComponent
import kotlininject.merge.app.deckbox.android.di.create
import kotlininject.merge.app.deckbox.android.di.createMergedAndroidAppComponent

class DeckBoxApplication : Application() {

  val component: MergedAndroidAppComponent by unsafeLazy {
    AndroidAppComponent.createMergedAndroidAppComponent(this)
  }

  override fun onCreate() {
    super.onCreate()
    Heartwood.grow(AndroidBark())
  }
}
