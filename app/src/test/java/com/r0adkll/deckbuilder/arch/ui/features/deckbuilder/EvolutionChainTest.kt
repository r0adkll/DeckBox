package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import org.amshove.kluent.*
import org.junit.Test


class EvolutionChainTest {

    @Test
    fun testAddBasicPokemon() {
        val pokemon = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val chain = EvolutionChain.create(pokemon)

        chain.nodes.size.shouldEqualTo(1)
        chain.contains(pokemon).shouldBeTrue()
        chain.nodes[0].name?.shouldEqualTo("Eevee")
        chain.nodes[0].cards.size.shouldEqualTo(1)
        chain.nodes[0].cards[0].shouldEqual(pokemon)
    }


    @Test
    fun testAddEvolutionPokemon() {
        val pokemon = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-6", name = "Espeon-GX", evolvesFrom = "Eevee")
        val chain = EvolutionChain.create(pokemon)

        chain.addCard(pokemonStage1)

        chain.nodes.size.shouldEqualTo(2)
        chain.contains(pokemon).shouldBeTrue()
        chain.contains(pokemonStage1).shouldBeTrue()
        chain.first()!!.name?.shouldEqualTo("Eevee")
        chain.last()!!.name?.shouldEqualTo("Espeon-GX")
    }


    @Test
    fun testAddBasicOutOfOrder() {
        val pokemon = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-6", name = "Espeon-GX", evolvesFrom = "Eevee")
        val chain = EvolutionChain.create(pokemonStage1)

        chain.addCard(pokemon)

        chain.nodes.size.shouldEqualTo(2)
        chain.contains(pokemon).shouldBeTrue()
        chain.contains(pokemonStage1).shouldBeTrue()
        chain.first().shouldNotBeNull()
        chain.first()?.name?.shouldEqualTo("Eevee")
        chain.last().shouldNotBeNull()
        chain.last()?.name?.shouldEqualTo("Espeon-GX")
    }


    @Test
    fun testAddMultipleBasic() {
        val pokemon1 = createPokemonCard().copy(id = "sm1-5", name = "Eevee")
        val pokemon2 = createPokemonCard().copy(id = "sm1-6", name = "Eevee")
        val pokemon3 = createPokemonCard().copy(id = "sm1-7", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-9", name = "Espeon-GX", evolvesFrom = "Eevee")
        val chain = EvolutionChain.create(pokemon1)

        chain.addCard(pokemonStage1)
        chain.addCard(pokemon2)
        chain.addCard(pokemon3)

        chain.nodes.size.shouldEqualTo(2)
        chain.contains(pokemon1)
        chain.contains(pokemon2)
        chain.contains(pokemon3)
        chain.contains(pokemonStage1)
        chain.first()!!.cards.size.shouldEqualTo(3)
        chain.first()!!.name?.shouldEqualTo("Eevee")
        chain.last()!!.cards.size.shouldEqualTo(1)
        chain.last()!!.name?.shouldEqualTo("Espeon-GX")
        chain.last()!!.evolvesFrom?.shouldEqualTo("Eevee")
    }


    @Test
    fun testIsChainForCard() {
        val pokemon1 = createPokemonCard().copy(id = "sm1-6", name = "Eevee")
        val pokemon2 = createPokemonCard().copy(id = "sm1-7", name = "Eevee")
        val pokemonStage1 = createPokemonCard().copy(id = "sm1-9", name = "Espeon-GX", evolvesFrom = "Eevee")
        val pokemonStage12 = createPokemonCard().copy(id = "sm1-10", name = "Umbreon-GX", evolvesFrom = "Eevee")
        val pokemonDiff = createPokemonCard().copy(id = "sm2-10", name = "Tapu Lele")

        val chain = EvolutionChain.create(pokemon1)
        chain.addCard(pokemonStage1)

        chain.isChainFor(pokemon2).shouldBeTrue()
        chain.isChainFor(pokemonStage12).shouldBeTrue()
        chain.isChainFor(pokemonDiff).shouldBeFalse()
    }


    @Test
    fun testBuildChainList() {
        val pokemons = listOf(
                createPokemonCard().copy(id = "sm1-6", name = "Eevee"),
                createPokemonCard().copy(id = "sm1-10", name = "Umbreon-GX", evolvesFrom = "Eevee"),
                createPokemonCard().copy(id = "sm1-11", name = "Espeon-GX", evolvesFrom = "Eevee"),
                createPokemonCard().copy(id = "sm2-20", name = "Tapu Lele")
        )

        val chains = EvolutionChain.build(pokemons)

        chains.size.shouldEqualTo(2)
        chains[0].nodes.size.shouldEqualTo(2)
        chains[1].nodes.size.shouldEqualTo(1)
    }
}