package app.deckbox.di

import app.deckbox.di.scopes.AppScope
import app.deckbox.di.scopes.MergeAppScope
import com.r0adkll.kotlininject.merge.annotations.MergeComponent

@AppScope
@MergeComponent(MergeAppScope::class)
abstract class AppComponent {

  companion object
}
