package app.deckbox.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import app.deckbox.common.screens.DecksScreen
import app.deckbox.shared.root.DeckBoxContent
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.push
import com.slack.circuit.foundation.rememberCircuitNavigator

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val backstack = rememberSaveableBackStack { push(DecksScreen()) }
      val navigator = rememberCircuitNavigator(backstack)

      DeckBoxContent(
        backstack = backstack,
        navigator = navigator,
      )
    }
  }
}
