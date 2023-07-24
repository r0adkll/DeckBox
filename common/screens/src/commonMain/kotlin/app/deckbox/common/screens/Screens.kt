package app.deckbox.common.screens

import com.slack.circuit.runtime.Screen

@CommonParcelize
class DecksScreen : DeckBoxScreen(name = "Decks()")

@CommonParcelize
class BrowseScreen : DeckBoxScreen(name = "Browse()")

@CommonParcelize
class ExpansionsScreen : DeckBoxScreen(name = "Expansions()")

abstract class DeckBoxScreen(val name: String) : Screen {
  open val arguments: Map<String, *>? = null
}
