package app.deckbox.playtest.api.model

sealed class PlayTestException(message: String? = null) : Exception(message) {

  /**
   * This exception occurs when the player draws (via turn draw or other) and there are no more cards in the deck
   */
  class DeckOutException : PlayTestException()
}
