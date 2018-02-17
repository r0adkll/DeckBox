package com.r0adkll.deckbuilder.arch.data.features.ptcgo.repository

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.ptcgo.repository.PTCGOConverter
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import io.pokemontcg.model.Type.*
import io.reactivex.Observable
import timber.log.Timber
import java.io.StringReader
import javax.inject.Inject


class DefaultPTCGOConverter @Inject constructor(
        val repository: CardRepository,
        val preferences: AppPreferences
) : PTCGOConverter {

    @SuppressLint("CheckResult")
    override fun import(deckList: String): Observable<List<PokemonCard>> {
        return repository.getExpansions()
                .onErrorReturnItem(emptyList())
                .flatMap {
                    val cards = parsePtcgoDeckList(it, deckList)
                    val ids = cards.map { it.id }
                    repository.find(ids)
                            .map { pokes ->
                                val allCards = ArrayList<PokemonCard>()
                                pokes.forEach { poke ->
                                    val count = cards.find { it.id == poke.id }?.count ?: 0
                                    (0 until count).forEach {
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
                                        val missingEnergyCards = missingEnergy.map(mapEnergy())
                                                .filter { it != null }
                                                .map { it!! }

                                        val energyIds = missingEnergyCards.map { it.second }

                                        Timber.d("Searching for default energy: $missingEnergy")

                                        // Now search for these missing default energy cards
                                        repository.find(energyIds)
                                                .map { pokes ->
                                                    pokes.forEach { poke ->
                                                        val count = missingEnergyCards.find { it.second == poke.id }?.first?.count ?: 0
                                                        Timber.d("* $count Energy($poke)")
                                                        (0 until count).forEach {
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

    override fun export(cards: List<PokemonCard>, name: String): Observable<String> {
        val pokemon = cards.filter { it.supertype == SuperType.POKEMON }
        val trainers = cards.filter { it.supertype == SuperType.TRAINER }
        val energy = cards.filter { it.supertype == SuperType.ENERGY }

        val listBuilder = StringBuilder()
        listBuilder
                .appendln("****** $name ******")
                .appendln()
                .appendln("##Pokémon - ${pokemon.size}")
                .appendln()

        val groupedPokemon = pokemon.groupBy { it.id }
        groupedPokemon.entries.forEach {
            val count = it.value.size
            val poke = it.value.first()
            listBuilder.appendln("* $count ${poke.name} ${poke.expansion!!.ptcgoCode} ${poke.number}")
        }

        listBuilder
                .appendln()
                .appendln("##Trainer Cards - ${trainers.size}")
                .appendln()

        val groupedTrainers = trainers.groupBy { it.id }
        groupedTrainers.entries.forEach {
            val count = it.value.size
            val trainer = it.value.first()
            listBuilder.appendln("* $count ${trainer.name} ${trainer.expansion!!.ptcgoCode} ${trainer.number}")
        }

        listBuilder
                .appendln()
                .appendln("##Energy - ${energy.size}")
                .appendln()

        val groupedEnergy = energy.groupBy { it.id }
        groupedEnergy.forEach {
            val count = it.value.count()
            val ener = it.value.first()
            listBuilder.appendln("* $count ${ener.name} ${ener.expansion!!.ptcgoCode} ${ener.number}")
        }

        listBuilder
                .appendln()
                .appendln("Total Cards - ${cards.size}")
                .appendln()
                .appendln("****** Deck List Generated by DeckBox ******")

        return Observable.just(listBuilder.toString())
    }


    private fun parsePtcgoDeckList(expansions: List<Expansion>, deckList: String): List<CardSpec> {
        val reader = StringReader(deckList)
        val lines = reader.readLines()

        val cards = ArrayList<CardSpec>()
        lines.forEach { line ->
            if (line.startsWith("* ")) {
                val count = parseCount(line)
                if (count != null) {
                    val setInfo = parseSetInformation(line)
                    val name = parseName(line)
                    val expansion = expansions.find { it.ptcgoCode == setInfo.first }
                    val id = "${expansion?.code}-${setInfo.second}"
                    cards.add(CardSpec(count, name, setInfo.first, setInfo.second, id))
                }
            }
        }

        return cards
    }


    private fun parseExpectedCount(line: String, prefix: String): Int {
        val remaining = line.replace(prefix, "").replace("-", "").trim()
        return remaining.toIntOrNull() ?: 0
    }


    private fun parseCount(line: String): Int? {
        val countPart = line.substring(2, line.indexOf(" ", 2))
        return countPart.toIntOrNull()
    }


    private fun parseSetInformation(line: String): Pair<String, String> {
        val parts = line.trim().split(" ")
        val number = parts.last()
        val set = parts[parts.size - 2]
        return Pair(set, number)
    }


    private fun parseName(line: String): String {
        val clean = line.substring(2).trim()
        val parts = clean.split(" ").toMutableList()
        val nameParts = parts.dropLast(2)
        return nameParts.joinToString(" ")
    }


    private fun filterEnergy(): (CardSpec) -> Boolean {
        return { spec ->
            Type.VALUES.filter { it != COLORLESS && it != UNKNOWN && it != DRAGON}
                    .find { spec.name.contains("${it.name} Energy", true) } != null
        }
    }


    private fun mapEnergy(): (CardSpec) -> Pair<CardSpec, String>? {
        return { spec ->
            val type = Type.VALUES.filter { it != COLORLESS && it != UNKNOWN && it != DRAGON }
                    .find { spec.name.contains("${it.name} Energy", true) }
            type?.let {
                val defaultEnergySet = preferences.basicEnergySet.get()
                defaultEnergySet.convert(it)?.let { Pair(spec, it) }
            }
        }
    }


    data class CardSpec(
            val count: Int,
            val name: String,
            val set: String,
            val number: String,
            val id: String
    )
}