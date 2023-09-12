package app.deckbox.common.screens

import app.deckbox.core.model.Card
import app.deckbox.core.model.SuperType
import com.slack.circuit.runtime.screen.Screen

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
class DeckPickerScreen : DeckBoxScreen(name = "DeckPicker()")

@CommonParcelize
data class DeckBuilderScreen(
  val id: String,
) : DeckBoxScreen(name = "DeckBuilder()") {
  override val arguments get() = mapOf("id" to id)

  @CommonIgnoredOnParcel
  override val presentation = Presentation(hideBottomNav = true)
}

@CommonParcelize
data class PlayTestScreen(
  val deckId: String,
) : DeckBoxScreen(name = "PlayTest()") {
  override val arguments get() = mapOf("deckId" to deckId)

  @CommonIgnoredOnParcel
  override val presentation = Presentation(hideBottomNav = true)
}

@CommonParcelize
class BoosterPackScreen : DeckBoxScreen(name = "BoosterPack()")

@CommonParcelize
data class BoosterPackBuilderScreen(
  val id: String,
) : DeckBoxScreen(name = "BoosterPackBuilder()") {
  override val arguments get() = mapOf("id" to id)

  @CommonIgnoredOnParcel
  override val presentation = Presentation(hideBottomNav = true)
}

@CommonParcelize
class BoosterPackPickerScreen : DeckBoxScreen(name = "BoosterPackPicker()")

@CommonParcelize
data class BrowseScreen(
  val deckId: String? = null,
  val packId: String? = null,
  val superType: SuperType? = null,
) : DeckBoxScreen(name = "Browse()") {
  override val arguments get() = mapOf(
    "deckId" to deckId,
    "packId" to packId,
    "superType" to superType,
  )

  @CommonIgnoredOnParcel
  override val presentation = Presentation(
    hideBottomNav = deckId != null || packId != null,
  )
}

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
  val deckId: String? = null,
  val packId: String? = null,
) : DeckBoxScreen(name = "CardDetail()") {
  constructor(
    card: Card,
    deckId: String? = null,
    packId: String? = null,
  ) : this(card.id, card.name, card.image.large, deckId, packId)

  override val arguments get() = mapOf(
    "cardId" to cardId,
    "cardName" to cardName,
    "cardImageLarge" to cardImageLarge,
    "deckId" to deckId,
    "packId" to packId,
  )

  @CommonIgnoredOnParcel
  override val presentation = Presentation(
    hideBottomNav = deckId != null || packId != null,
  )
}

@CommonParcelize
class FilterScreen : DeckBoxScreen(name = "Filter()")

@CommonParcelize
class SettingsScreen : DeckBoxScreen(name = "Settings()")

//region Utility Screens

@CommonParcelize
data class UrlScreen(val url: String) : DeckBoxScreen(name = "UrlScreen()") {
  override val arguments get() = mapOf("url" to url)
}

/**
 * This is a hack to pass data back from a screen that is in an Overlay container to
 * be returned in the [com.slack.circuit.overlay.OverlayNavigator]
 */
@CommonParcelize
data class OverlayResultScreen<T>(
  @CommonIgnoredOnParcel val result: T? = null,
) : Screen

//endregion

abstract class DeckBoxScreen(val name: String) : Screen {
  open val arguments: Map<String, *>? = null
  open val presentation: Presentation = Presentation()
}

data class Presentation(
  val hideBottomNav: Boolean = false,
  val isDetailScreen: Boolean = false,
)
