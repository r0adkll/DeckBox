package app.deckbox.core.model

/**
 * Deck validation error
 * @param message the validation error message
 * @param isBlocking whether or not this error should block other actions that require a valid deck
 */
data class ValidationError(
  val message: Message,
  val isBlocking: Boolean = false
) {

  sealed class Message {
    class Resource(val resId: Int, vararg val args: String) : Message()
    class Literal(val message: String) : Message()
  }
}
