package com.r0adkll.deckbuilder.internal.analytics


sealed class Event {

    sealed class Login : Event() {
        object Google : Login()
        object Anonymous : Login()
        object Offline : Login()
    }

    sealed class SignUp : Event() {
        object Google : SignUp()
    }

    class Search(val term: String) : Event()
    class Share(val type: String, val id: String = "") : Event()

    object TutorialBegin : Event()
    object TutorialComplete : Event()

    sealed class SelectContent(
            val type: String,
            open val id: String,
            open val name: String? = null,
            open val value: Long? = null
    ) : Event() {
        class PokemonCard(override val id: String) : SelectContent("pokemon_card", id)
        class Deck(override val id: String) : SelectContent("deck", id)

        class Action(override val id: String, override val name: String? = null) : SelectContent("action", id, name)
        class MenuAction(override val id: String) : SelectContent("menu_action", id)
        class FilterOption(
                override val id: String,
                override val name: String?,
                override val value: Long? = null
        ) : SelectContent("filter_option", id, name, value)

        class MissingCard(override val id: String) : SelectContent("missing_card", id)
    }
}