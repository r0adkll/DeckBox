package com.r0adkll.deckbuilder.arch.data.features.importer.repository

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.importer.model.CardSpec
import com.r0adkll.deckbuilder.arch.data.features.importer.parser.DeckListParser
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.importer.repository.Importer
import io.pokemontcg.model.Type
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DefaultImporter @Inject constructor(
    val cardRepository: CardRepository,
    val expansionRepository: ExpansionRepository,
    val preferences: AppPreferences
) : Importer {

    private val parser = DeckListParser()

    override fun import(deckList: String): Observable<List<PokemonCard>> {
        return expansionRepository.getExpansions()
            .onErrorReturnItem(emptyList())
            .flatMap { it ->
                val cards = parser.parse(it, deckList)
                val ids = cards.map { it.id }
                cardRepository.find(ids)
                    .map { pokes ->
                        val allCards = ArrayList<PokemonCard>()
                        pokes.forEach { poke ->
                            val count = cards.find { it.id == poke.id }?.count ?: 0
                            (0 until count).forEach { _ ->
                                allCards.add(poke.copy())
                            }
                        }
                        allCards
                    }
                    .flatMap { allCards ->
                        val expectedCount = cards.sumBy { it.count }
                        if (expectedCount != allCards.size) {
                            Timber.w("Uh-oh! It looks like we haven't imported all of the cards!")

                            // Find missing cards
                            val missing = cards.filter { spec ->
                                allCards.find { it.id == spec.id } == null
                            }

                            // Determine if any are energy cards
                            val missingEnergy = missing.filter(filterEnergy())
                            if (missingEnergy.isNotEmpty()) {
                                val missingEnergyCards = missingEnergy
                                    .mapNotNull(mapEnergy())
                                    .map { it }

                                val energyIds = missingEnergyCards.map { it.second }

                                Timber.d("Searching for default energy: $missingEnergy")

                                // Now search for these missing default energy cards
                                cardRepository.find(energyIds)
                                    .map { pokes ->
                                        pokes.forEach { poke ->
                                            val count = missingEnergyCards.find { it.second == poke.id }?.first?.count
                                                ?: 0
                                            Timber.d("* $count Energy($poke)")
                                            (0 until count).forEach { _ ->
                                                allCards.add(poke.copy())
                                            }
                                        }
                                        allCards
                                    }
                            } else {
                                Observable.just(allCards)
                            }
                        } else {
                            Observable.just(allCards)
                        }
                    }
                    .doOnNext { Timber.d("Pokemon Found: $it") }
            }
    }

    private fun filterEnergy(): (CardSpec) -> Boolean {
        return { spec ->
            Type.VALUES.filter { it != Type.COLORLESS && it != Type.UNKNOWN && it != Type.DRAGON }
                .find {
                    spec.name.contains("${it.name} Energy", true) ||
                        (spec.name.contains(it.name, true) && spec.set.contains("Energy", true))
                } != null
        }
    }

    private fun mapEnergy(): (CardSpec) -> Pair<CardSpec, String>? {
        return { spec ->
            val type = Type.VALUES.filter { it != Type.COLORLESS && it != Type.UNKNOWN && it != Type.DRAGON }
                .find {
                    spec.name.contains("${it.name} Energy", true) ||
                        (spec.name.contains(it.name, true) && spec.set.contains("Energy", true))
                }
            type?.let {
                val defaultEnergySet = preferences.basicEnergySet.get()
                defaultEnergySet.convert(it)?.let { Pair(spec, it) }
            }
        }
    }
}
