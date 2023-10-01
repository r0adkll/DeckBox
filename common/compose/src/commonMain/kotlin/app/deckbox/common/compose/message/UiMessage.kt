package app.deckbox.common.compose.message

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class UiMessage(
  val id: Long = uuid4().mostSignificantBits,
  val text: @Composable () -> String,
)

fun UiMessage(
  t: Throwable,
  id: Long = uuid4().mostSignificantBits,
): UiMessage = UiMessage(
  text = { t.message ?: "Error occurred: $t" },
  id = id,
)

fun UiMessageManager.showUiMessage(scope: CoroutineScope, text: @Composable () -> String) {
  scope.launch {
    emitMessage(
      UiMessage(text = text)
    )
  }
}

@Composable
fun UiMessageSnackBarEmitter(
  uiMessage: UiMessage?,
  snackbarHostState: SnackbarHostState,
  onEmit: (UiMessage) -> Unit,
) {
  uiMessage?.let { message ->
    val messageText = message.text()
    LaunchedEffect(message) {
      snackbarHostState.showSnackbar(
        message = messageText,
      )
      onEmit(message)
    }
  }
}

class UiMessageManager {
  private val mutex = Mutex()

  private val _messages = MutableStateFlow(emptyList<UiMessage>())

  /**
   * A flow emitting the current message to display.
   */
  val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

  suspend fun emitMessage(message: UiMessage) {
    mutex.withLock {
      _messages.value = _messages.value + message
    }
  }

  suspend fun clearMessage(id: Long) {
    mutex.withLock {
      _messages.value = _messages.value.filterNot { it.id == id }
    }
  }
}
