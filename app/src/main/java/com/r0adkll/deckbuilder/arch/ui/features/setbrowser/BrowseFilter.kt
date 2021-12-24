package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.os.Parcelable
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.SubTypes.BREAK
import com.r0adkll.deckbuilder.arch.domain.SubTypes.EX
import com.r0adkll.deckbuilder.arch.domain.SubTypes.GX
import com.r0adkll.deckbuilder.arch.domain.SubTypes.ITEM
import com.r0adkll.deckbuilder.arch.domain.SubTypes.MEGA
import com.r0adkll.deckbuilder.arch.domain.SubTypes.POKEMON_TOOL
import com.r0adkll.deckbuilder.arch.domain.SubTypes.RAPID_STRIKE
import com.r0adkll.deckbuilder.arch.domain.SubTypes.SINGLE_STRIKE
import com.r0adkll.deckbuilder.arch.domain.SubTypes.STADIUM
import com.r0adkll.deckbuilder.arch.domain.SubTypes.SUPPORTER
import com.r0adkll.deckbuilder.arch.domain.SubTypes.TAG_TEAM
import com.r0adkll.deckbuilder.arch.domain.SubTypes.V
import com.r0adkll.deckbuilder.arch.domain.SubTypes.VMAX
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.CardUtils
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.SuperType.ENERGY
import io.pokemontcg.model.SuperType.POKEMON
import io.pokemontcg.model.SuperType.TRAINER
import kotlinx.android.parcel.Parcelize

@Suppress("MagicNumber")
sealed class BrowseFilter : Parcelable {

  abstract val title: Int
  abstract val weight: Int
  open val titleLiteral: String? = null

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
  data class SubTypeBrowseFilter(private val subType: String) : BrowseFilter() {

    override val titleLiteral = subType
    override val title: Int = -1

    override val weight: Int
      get() = when (subType.uppercase()) {
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
        SubTypeBrowseFilter(POKEMON_TOOL),
        SubTypeBrowseFilter(BREAK),
        SubTypeBrowseFilter(TAG_TEAM),
        SubTypeBrowseFilter(V),
        SubTypeBrowseFilter(VMAX),
        SubTypeBrowseFilter(RAPID_STRIKE),
        SubTypeBrowseFilter(SINGLE_STRIKE),
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
