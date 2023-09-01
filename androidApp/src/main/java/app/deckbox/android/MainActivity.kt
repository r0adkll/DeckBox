package app.deckbox.android

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import app.deckbox.android.di.ActivityComponent
import app.deckbox.android.di.AndroidAppComponent
import app.deckbox.android.di.from
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.di.ActivityScope
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.shared.di.UiComponent
import app.deckbox.shared.root.DeckBoxContent
import com.r0adkll.kotlininject.merge.annotations.ContributesSubcomponent
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import me.tatarka.inject.annotations.Provides

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val component = AndroidAppComponent.from(this)
      .createMainActivityComponent(this)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val backstack = rememberSaveableBackStack { push(DecksScreen()) }
      val navigator = rememberCircuitNavigator(backstack)

      component.deckBoxContent(
        backstack,
        navigator,
        { url ->
          val intent = CustomTabsIntent.Builder().build()
          intent.launchUrl(this@MainActivity, Uri.parse(url))
        },
        Modifier,
      )
    }
  }
}

@ActivityScope
@ContributesSubcomponent(
  scope = MergeActivityScope::class,
  parentScope = MergeAppScope::class,
)
abstract class MainActivityComponent(
  @get:Provides override val activity: Activity,
) : ActivityComponent, UiComponent {
  abstract val deckBoxContent: DeckBoxContent
}
