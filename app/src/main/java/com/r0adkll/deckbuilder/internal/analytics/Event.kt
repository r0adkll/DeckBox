package com.r0adkll.deckbuilder.internal.analytics

import com.r0adkll.deckbuilder.arch.domain.features.remote.model.SearchProxies


sealed class Event {

    sealed class Login : Event() {
        object Google : Login()
        object Anonymous : Login()
        object Offline : Login()
    }

    sealed class SignUp : Event() {
        object Google : SignUp()
    }

    object Logout : Event()

    data class Search(val term: String) : Event()
    data class SearchProxy(val proxy: SearchProxies.Proxy) : Event()
    data class Share(val type: String, val id: String = "") : Event()

    object TutorialBegin : Event()
    object TutorialComplete : Event()

    sealed class SelectContent(
            val type: String,
            open val id: String,
            open val name: String? = null,
            open val value: Long? = null
    ) : Event() {
        data class PokemonCard(override val id: String) : SelectContent("pokemon_card", id)
        data class Deck(override val id: String) : SelectContent("deck", id)

        data class Action(override val id: String, override val name: String? = null, override val value: Long? = null) : SelectContent("action", id, name, value)
        data class MenuAction(override val id: String, override val value: Long? = null) : SelectContent("menu_action", id, value = value)
        data class FilterOption(
                override val id: String,
                override val name: String?,
                override val value: Long? = null
        ) : SelectContent("filter_option", id, name, value)

        data class MissingCard(override val id: String) : SelectContent("missing_card", id)
    }
}