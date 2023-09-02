// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.logging.bark
import app.deckbox.shared.DesktopApplicationComponent
import app.deckbox.shared.WindowComponent
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import java.awt.Desktop
import java.net.URI
import kotlininject.merge.app.deckbox.shared.createMergedDesktopApplicationComponent

@Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST", "USELESS_CAST", "KotlinRedundantDiagnosticSuppress")
fun main() = application {
  val applicationComponent = remember {
    DesktopApplicationComponent.createMergedDesktopApplicationComponent()
  }

//    LaunchedEffect(applicationComponent) {
//        applicationComponent.initializers.initialize()
//    }

  Window(
    title = "DeckBox",
    onCloseRequest = ::exitApplication,
  ) {
    val component: WindowComponent = remember(applicationComponent) {
      applicationComponent.createWindowComponent() as WindowComponent
    }

    val backstack = rememberSaveableBackStack { push(DecksScreen()) }
    val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }

    component.deckBoxContent(
      backstack,
      navigator,
      { url ->
        try {
          val uri = URI(url)
          val dt = Desktop.getDesktop()
          dt.browse(uri)
        } catch (ex: Exception) {
          bark(throwable = ex) { "Unable to open URL" }
        }
      },
      WindowInsets(
        top = 24.dp,
        bottom = 24.dp,
      ),
      Modifier,
    )
  }
}
