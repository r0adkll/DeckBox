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

    sealed class ViewItem(
            open val id: String,
            open val name: String,
            open val category: String
    ) : Event() {

        data class MarketplaceLink(
                override val id: String,
                override val name: String = "TCGPlayer"
        ) : ViewItem(id, name, "marketplace")
    }

    sealed class SelectContent(
            val type: String,
            open val id: String,
            open val name: String? = null,
            open val value: Long? = null
    ) : Event() {

        data class PokemonCard(override val id: String) : SelectContent("pokemon_card", id)

        sealed class Deck(override val id: String, override val name: String? = null) : SelectContent("deck", id) {
            object Opened : Deck("open")
            object EditName : Deck("change_name")
            object EditDescription : Deck("change_description")
            object EditImage : Deck("change_image")
            data class EditCollectionOnly(val enabled: Boolean) : Deck("change_collection_only", if (enabled) "on" else "off")
        }

        data class CollectionExpansionSet(override val name: String) : SelectContent("expansion", "collection")
        data class BrowseExpansionSet(override val name: String) : SelectContent("expansion", "browse")

        sealed class Collection(override val id: String, override val name: String?) : SelectContent("collection", id) {
            data class Increment(val cardId: String) : Collection("increment", cardId)
            data class Decrement(val cardId: String) : Collection("decrement", cardId)
        }

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
