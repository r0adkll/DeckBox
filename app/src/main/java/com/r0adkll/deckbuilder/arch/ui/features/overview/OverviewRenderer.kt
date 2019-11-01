package com.r0adkll.deckbuilder.arch.ui.features.overview

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.stack
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Scheduler
import java.util.*

class OverviewRenderer(
    actions: OverviewUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : UiBaseStateRenderer<OverviewUi.State, OverviewUi.State.Change, OverviewUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
            .map { it.cards.stack() }
            .map { EvolutionChain.build(it) }
            .map {
                it.sortedWith(Comparator { lhs, rhs ->
                    // 0 - Sort by node size
                    val r0 = rhs.size.compareTo(lhs.size)
                    if (r0 == 0) {
                        // 1 - Sort by SuperType
                        val lhsCard = lhs.first()?.cards?.firstOrNull()?.card
                        val rhsCard = rhs.first()?.cards?.firstOrNull()?.card
                        if (lhsCard != null && rhsCard != null) {
                            val r1 = compareSupertype(lhsCard, rhsCard)
                            if (r1 == 0) {
                                // 2 - Based on SuperType, sort by type characteristics
                                when (lhsCard.supertype) {
                                    SuperType.POKEMON -> {
                                        // 2a - Pokemon - Sort National Dex Number
                                        lhsCard.nationalPokedexNumber?.compareTo(rhsCard.nationalPokedexNumber
                                            ?: Int.MAX_VALUE) ?: 0
                                    }
                                    SuperType.TRAINER -> {
                                        // 2b - Trainer -> Item > Supporter > Stadium > Tool
                                        compareSubtype(lhsCard, rhsCard)
                                    }
                                    SuperType.ENERGY -> {
                                        // 2c - Energy - Sort by subtype (Special > Basic)
                                        compareSubtype(lhsCard, rhsCard)
                                    }
                                    else -> 0
                                }
                            } else {
                                r1
                            }
                        } else {
                            r0
                        }
                    } else {
                        r0
                    }
                })
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showCards(it) }
    }

    private fun compareSupertype(lhs: PokemonCard, rhs: PokemonCard): Int {
        return lhs.supertype.weight().compareTo(rhs.supertype.weight())
    }

    private fun compareSubtype(lhs: PokemonCard, rhs: PokemonCard): Int {
        return lhs.subtype.weight(lhs.supertype).compareTo(rhs.subtype.weight(rhs.supertype))
    }

    private fun SuperType.weight(): Int = when (this) {
        SuperType.POKEMON -> 0
        SuperType.TRAINER -> 1
        else -> 2
    }

    private fun SubType.weight(superType: SuperType): Int = when (superType) {
        SuperType.ENERGY -> when (this) {
            SubType.BASIC -> 1
            SubType.SPECIAL -> 0
            else -> 0
        }
        SuperType.TRAINER -> when (this) {
            SubType.ITEM -> 0
            SubType.SUPPORTER -> 1
            SubType.STADIUM -> 2
            SubType.POKEMON_TOOL -> 3
            else -> 4
        }
        else -> 0
    }
}
