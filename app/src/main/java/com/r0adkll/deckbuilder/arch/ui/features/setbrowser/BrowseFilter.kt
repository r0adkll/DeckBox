package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.os.Parcelable
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.CardUtils
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SubType.BREAK
import io.pokemontcg.model.SubType.EX
import io.pokemontcg.model.SubType.GX
import io.pokemontcg.model.SubType.ITEM
import io.pokemontcg.model.SubType.MEGA
import io.pokemontcg.model.SubType.POKEMON_TOOL
import io.pokemontcg.model.SubType.STADIUM
import io.pokemontcg.model.SubType.SUPPORTER
import io.pokemontcg.model.SubType.TAG_TEAM
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.SuperType.ENERGY
import io.pokemontcg.model.SuperType.POKEMON
import io.pokemontcg.model.SuperType.TRAINER
import kotlinx.android.parcel.Parcelize

@Suppress("MagicNumber")
sealed class BrowseFilter : Parcelable {

    abstract val title: Int
    abstract val weight: Int

    abstract fun apply(card: PokemonCard): Boolean

    @Parcelize
    class AllFilter : BrowseFilter() {

        override val title: Int
            get() = R.string.tab_all

        override val weight: Int
            get() = 0

        override fun apply(card: PokemonCard): Boolean = true
    }

    @Parcelize
    data class SuperTypeBrowseFilter(private val superType: SuperType) : BrowseFilter() {

        override val title: Int
            get() = when (superType) {
                ENERGY -> R.string.tab_energy
                POKEMON -> R.string.tab_pokemon
                TRAINER -> R.string.tab_trainer
                else -> -1
            }

        override val weight: Int
            get() = when (superType) {
                ENERGY -> 3
                POKEMON -> 1
                TRAINER -> 2
                else -> 0
            }

        override fun apply(card: PokemonCard): Boolean {
            return card.supertype == superType
        }
    }

    @Parcelize
    data class SubTypeBrowseFilter(private val subType: SubType) : BrowseFilter() {

        override val title: Int
            get() = when (subType) {
                EX -> R.string.tab_ex
                MEGA -> R.string.tab_mega
                ITEM -> R.string.tab_item
                STADIUM -> R.string.tab_stadium
                SUPPORTER -> R.string.tab_supporter
                GX -> R.string.tab_gx
                POKEMON_TOOL -> R.string.tab_tool
                BREAK -> R.string.tab_break
                TAG_TEAM -> R.string.tab_tagteam
                else -> -1
            }

        override val weight: Int
            get() = when (subType) {
                GX, EX, MEGA, BREAK -> 4
                TAG_TEAM -> 5
                ITEM -> 6
                SUPPORTER -> 7
                STADIUM -> 8
                else -> 9
            }

        override fun apply(card: PokemonCard): Boolean {
            return card.subtype == subType
        }
    }

    @Parcelize
    class PrismStarFilter : BrowseFilter() {

        override val title: Int
            get() = R.string.tab_prism

        override val weight: Int
            get() = 4

        override fun apply(card: PokemonCard): Boolean {
            return card.name.contains(CardUtils.PRISM_SYMBOL)
        }
    }

    companion object {

        private val REQUIRED = listOf(
            AllFilter(),
            SuperTypeBrowseFilter(POKEMON),
            SuperTypeBrowseFilter(TRAINER),
            SuperTypeBrowseFilter(ENERGY)
        )

        private val OPTIONAL by lazy {
            listOf(
                SubTypeBrowseFilter(GX),
                SubTypeBrowseFilter(MEGA),
                SubTypeBrowseFilter(ITEM),
                SubTypeBrowseFilter(STADIUM),
                SubTypeBrowseFilter(SUPPORTER),
                SubTypeBrowseFilter(GX),
                SubTypeBrowseFilter(POKEMON_TOOL),
                SubTypeBrowseFilter(BREAK),
                SubTypeBrowseFilter(TAG_TEAM),
                PrismStarFilter()
            )
        }

        fun findAvailable(cards: List<PokemonCard>): List<BrowseFilter> {
            val filters = HashSet<BrowseFilter>()

            cards.forEach { card ->
                filters += OPTIONAL.filter { it.apply(card) }
            }

            return REQUIRED + filters.toList()
        }
    }
}
