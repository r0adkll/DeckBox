package app.deckbox.common.screens

import app.deckbox.core.model.Card
import com.slack.circuit.runtime.Screen

/**
 * This is a dummy screen to fill a blank detail side content since the backstack/navigator requires a
 * non-empty state to be initialized. This will just render a blank screen but will be used to automatically
 * show/hide the side detail content pain
 */
@CommonParcelize
class RootScreen : DeckBoxScreen(name = "Root")

@CommonParcelize
class DecksScreen : DeckBoxScreen(name = "Decks()")

@CommonParcelize
class BrowseScreen : DeckBoxScreen(name = "Browse()")

@CommonParcelize
class ExpansionsScreen : DeckBoxScreen(name = "Expansions()")

@CommonParcelize
class ExpansionDetailScreen(
  val expansionId: String,
) : DeckBoxScreen(name = "ExpansionDetail()") {
  override val arguments get() = mapOf("expansionId" to expansionId)

  /*
   * This should push this screen during navigation into a sidePanel
   * content navigator so it can be displayed side-by-side to the screen
   * that pushed this one
   */
  @CommonIgnoredOnParcel
  override val presentation = Presentation(isDetailScreen = true)
}

@CommonParcelize
class CardDetailScreen(
  val cardId: String,
  val cardName: String,
  val cardImageLarge: String,
) : DeckBoxScreen(name = "CardDetail()") {
  constructor(card: Card) : this(card.id, card.name, card.image.large)

  override val arguments get() = mapOf("cardId" to cardId)
}

@CommonParcelize
class SettingsScreen : DeckBoxScreen(name = "Settings()")

abstract class DeckBoxScreen(val name: String) : Screen {
  open val arguments: Map<String, *>? = null
  open val presentation: Presentation = Presentation()
}

data class Presentation(
  val hideBottomNav: Boolean = false,
  val isDetailScreen: Boolean = false,
)
