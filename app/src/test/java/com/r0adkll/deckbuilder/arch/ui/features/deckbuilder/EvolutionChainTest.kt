package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldNotEqual
import org.junit.Test

/* ktlint-disable max-line-length */
class EvolutionChainTest {

    @Test
    fun testListEquality() {
        val chain1 = EvolutionChain.build(listOf(
            createPokemonCard().copy(id = "sm1-1", name = "Eevee").stack(collection = 1)
        ))
        val chain2 = EvolutionChain.build(listOf(
            createPokemonCard().copy(id = "sm1-1", name = "Eevee").stack(collection = 1)
        ))

        chain1 shouldEqual chain2
    }

    @Test
    fun testListInequalityViaCollectionCount() {
        val chain1 = EvolutionChain.build(listOf(
            createPokemonCard().copy(id = "sm1-1", name = "Eevee").stack(collection = 1)
        ))
        val chain2 = EvolutionChain.build(listOf(
            createPokemonCard().copy(id = "sm1-1", name = "Eevee").stack(collection = 4)
        ))

        chain1 shouldNotEqual chain2
    }

    @Test
    fun testAddBasicPokemon() {
        val pokemon = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val chain = EvolutionChain.create(pokemon.stack())

        chain.nodes.size.shouldEqualTo(1)
        chain.contains(pokemon.stack()).shouldBeTrue()
        chain.nodes[0].name?.shouldBeEqualTo("Eevee")
        chain.nodes[0].cards.size.shouldEqualTo(1)
        chain.nodes[0].cards[0].shouldEqual(pokemon.stack())
    }

    @Test
    fun testAddEvolutionPokemon() {
        val pokemon = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-6", name = "Espeon-GX", evolvesFrom = "Eevee")
        val chain = EvolutionChain.create(pokemon.stack())

        chain.addCard(pokemonStage1.stack())

        chain.nodes.size.shouldEqualTo(2)
        chain.contains(pokemon.stack()).shouldBeTrue()
        chain.contains(pokemonStage1.stack()).shouldBeTrue()
        chain.first()!!.name?.shouldBeEqualTo("Eevee")
        chain.last()!!.name?.shouldBeEqualTo("Espeon-GX")
    }

    @Test
    fun testAddBasicOutOfOrder() {
        val pokemon = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-6", name = "Espeon-GX", evolvesFrom = "Eevee")
        val chain = EvolutionChain.create(pokemonStage1.stack())

        chain.addCard(pokemon.stack())

        chain.nodes.size.shouldEqualTo(2)
        chain.contains(pokemon.stack()).shouldBeTrue()
        chain.contains(pokemonStage1.stack()).shouldBeTrue()
        chain.first().shouldNotBeNull()
        chain.first()?.name?.shouldBeEqualTo("Eevee")
        chain.last().shouldNotBeNull()
        chain.last()?.name?.shouldBeEqualTo("Espeon-GX")
    }

    @Test
    fun testAddMultipleBasic() {
        val pokemon1 = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val pokemon2 = createPokemonCard().copy(id = "sm1-6", name = "Eevee")
        val pokemon3 = createPokemonCard().copy(id = "sm1-7", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-9", name = "Espeon-GX", evolvesFrom = "Eevee")
        val chain = EvolutionChain.create(pokemon1.stack())

        chain.addCard(pokemonStage1.stack())
        chain.addCard(pokemon2.stack())
        chain.addCard(pokemon3.stack())

        chain.nodes.size.shouldEqualTo(2)
        chain.contains(pokemon1.stack())
        chain.contains(pokemon2.stack())
        chain.contains(pokemon3.stack())
        chain.contains(pokemonStage1.stack())
        chain.first()!!.cards.size.shouldEqualTo(3)
        chain.first()!!.name?.shouldBeEqualTo("Eevee")
        chain.last()!!.cards.size.shouldEqualTo(1)
        chain.last()!!.name?.shouldBeEqualTo("Espeon-GX")
        chain.last()!!.evolvesFrom?.shouldBeEqualTo("Eevee")
    }

    @Test
    fun testIsChainForCard() {
        val pokemon1 = createPokemonCard().copy(id = "sm1-6", name = "Eevee")
        val pokemon2 = createPokemonCard().copy(id = "sm1-7", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-9", name = "Espeon-GX", evolvesFrom = "Eevee")
        val pokemonStage12 = createPokemonCard().copy(id = "sm1-10", name = "Umbreon-GX", evolvesFrom = "Eevee")
        val pokemonDiff = createPokemonCard().copy(id = "sm2-10", name = "Tapu Lele")

        val chain = EvolutionChain.create(pokemon1.stack())
        chain.addCard(pokemonStage1.stack())

        chain.isChainFor(pokemon2.stack()).shouldBeTrue()
        chain.isChainFor(pokemonStage12.stack()).shouldBeTrue()
        chain.isChainFor(pokemonDiff.stack()).shouldBeFalse()
    }

    @Test
    fun testBuildChainList() {
        val pokemons = listOf(
            createPokemonCard().copy(id = "sm1-6", name = "Eevee"),
            createPokemonCard().copy(id = "sm1-10", name = "Umbreon-GX", evolvesFrom = "Eevee"),
            createPokemonCard().copy(id = "sm1-11", name = "Espeon-GX", evolvesFrom = "Eevee"),
            createPokemonCard().copy(id = "sm2-20", name = "Tapu Lele")
        )

        val chains = EvolutionChain.build(pokemons.map { it.stack() })

        chains.size.shouldEqualTo(2)
        chains[0].nodes.size.shouldEqualTo(2)
        chains[1].nodes.size.shouldEqualTo(1)
    }

    @Test
    fun testStage2OutOfOrderChainBuilding() {
        val pokemons = listOf(
            createPokemonCard().copy(id = "sm3-10", name = "Alolan Golem-GX", evolvesFrom = "Alolan Graveler", nationalPokedexNumber = 10).stack(),
            createPokemonCard().copy(id = "sm3-9", name = "Alolan Graveler", evolvesFrom = "Alolan Geodude", nationalPokedexNumber = 9).stack(),
            createPokemonCard().copy(id = "sm3-8", name = "Alolan Geodude", nationalPokedexNumber = 8).stack()
        )

        val chains = EvolutionChain.build(pokemons)

        chains.size.shouldEqualTo(1)
        chains[0].nodes.size.shouldEqualTo(3)
        chains[0].nodes[0].cards.contains(pokemons[2]).shouldBeTrue()
        chains[0].nodes[1].cards.contains(pokemons[1]).shouldBeTrue()
        chains[0].nodes[2].cards.contains(pokemons[0]).shouldBeTrue()
    }

    @Test
    fun testShortStage2OutOfOrderChainBuilding() {
        val pokemons = listOf(
            createPokemonCard().copy(id = "sm3-10", name = "Alolan Golem-GX", evolvesFrom = "Alolan Graveler", nationalPokedexNumber = 10).stack(),
            createPokemonCard().copy(id = "sm3-9", name = "Alolan Graveler", evolvesFrom = "Alolan Geodude", nationalPokedexNumber = 9).stack()
        )

        val chains = EvolutionChain.build(pokemons)

        chains.size.shouldEqualTo(1)
        chains[0].nodes.size.shouldEqualTo(2)
        chains[0].nodes[0].cards.contains(pokemons[1]).shouldBeTrue()
        chains[0].nodes[1].cards.contains(pokemons[0]).shouldBeTrue()
    }

    @Test
    fun testBreakEvolutionChain() {
        val pokemons = listOf(
            createPokemonCard().copy(id = "xy11-6", name = "Yanma", nationalPokedexNumber = 193).stack(),
            createPokemonCard().copy(id = "xy11-8", name = "Yanmega BREAK", nationalPokedexNumber = 469, evolvesFrom = "Yanmega").stack(),
            createPokemonCard().copy(id = "xy11-7", name = "Yanmega", nationalPokedexNumber = 469, evolvesFrom = "Yanma").stack()
        )

        val chains = EvolutionChain.build(pokemons)

        chains.size shouldEqualTo 1
        chains[0].nodes.size shouldEqualTo 3
        chains[0].nodes[0].name!!.shouldBeEqualTo("Yanma")
        chains[0].nodes[1].name!!.shouldBeEqualTo("Yanmega")
        chains[0].nodes[2].name!!.shouldBeEqualTo("Yanmega BREAK")
    }

    @Test
    fun testCommonFossilChains() {
        val pokemons = listOf(
            createPokemonCard("Omanyte").copy(id = "sm5-100", nationalPokedexNumber = 138, evolvesFrom = "Unidentified Fossil").stack(),
            createPokemonCard("Aerodactyl-GX").copy(id = "sm11-106", nationalPokedexNumber = 142, evolvesFrom = "Unidentified Fossil").stack(),
            createPokemonCard("Cranidos").copy(id = "sm5-64", nationalPokedexNumber = 408, evolvesFrom = "Unidentified Fossil").stack(),
            createPokemonCard("Omastar").copy(id = "sm9-76", nationalPokedexNumber = 139, evolvesFrom = "Omanyte").stack()
        )
        val chains = EvolutionChain.build(pokemons)

        chains.size shouldEqualTo 3
        chains[0].nodes.size shouldEqualTo 2
        chains[1].nodes.size shouldEqualTo 1
        chains[2].nodes.size shouldEqualTo 1
        chains[0].nodes[0].name!!.shouldBeEqualTo("Omanyte")
        chains[0].nodes[1].name!!.shouldBeEqualTo("Omastar")
    }

    fun PokemonCard.stack(count: Int = 1, collection: Int? = null): StackedPokemonCard = StackedPokemonCard(this, count, collection)
}
/* ktlint-enable max-line-length */
