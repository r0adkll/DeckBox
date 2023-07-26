package app.deckbox.android

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import app.deckbox.android.di.ActivityComponent
import app.deckbox.android.di.AndroidAppComponent
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.di.ActivityScope
import app.deckbox.shared.di.UiComponent
import app.deckbox.shared.root.DeckBoxContent
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.push
import com.slack.circuit.foundation.rememberCircuitNavigator
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val component = MainActivityComponent.create(this)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val backstack = rememberSaveableBackStack { push(DecksScreen()) }
      val navigator = rememberCircuitNavigator(backstack)

      component.deckBoxContent(
        backstack = backstack,
        navigator = navigator,
        modifier = Modifier,
      )
    }
  }
}

@ActivityScope
@Component
abstract class MainActivityComponent(
  @get:Provides override val activity: Activity,
  @Component val applicationComponent: AndroidAppComponent = AndroidAppComponent.from(activity),
) : ActivityComponent, UiComponent {
  abstract val deckBoxContent: DeckBoxContent

  companion object
}
