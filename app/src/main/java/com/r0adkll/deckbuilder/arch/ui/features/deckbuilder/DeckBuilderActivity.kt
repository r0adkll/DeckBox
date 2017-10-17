package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.CardSet
import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.DeckBuilderPagerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import kotlinx.android.synthetic.main.activity_deck_builder.*


class DeckBuilderActivity : BaseActivity() {

    private val pokemonCardClicks: Relay<PokemonCard> = PublishRelay.create()

    private lateinit var adapter: DeckBuilderPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)

        adapter = DeckBuilderPagerAdapter(layoutInflater, pokemonCardClicks)
        pager.adapter = adapter
        pager.offscreenPageLimit = 3

        pager.post {
            val pokemons = listOf(
                    createPokemonCard().copy(id = "sm1-6", name = "Eevee", imageUrl = "https://images.pokemontcg.io/dp5/62.png"),
                    createPokemonCard().copy(id = "sm1-10", name = "Umbreon-GX", evolvesFrom = "Eevee", imageUrl = "https://images.pokemontcg.io/sm1/80.png"),
                    createPokemonCard().copy(id = "sm1-11", name = "Espeon-GX", evolvesFrom = "Eevee", imageUrl = "https://images.pokemontcg.io/sm1/61.png"),
                    createPokemonCard().copy(id = "sm2-20", name = "Tapu Lele", imageUrl = "https://images.pokemontcg.io/sm2/60.png"),
                    createPokemonCard().copy(id = "sm3-1", name = "Charmander", imageUrl = "https://images.pokemontcg.io/sm3/18.png"),
                    createPokemonCard().copy(id = "sm3-2", name = "Charmeleon", imageUrl = "https://images.pokemontcg.io/sm3/19.png", evolvesFrom = "Charmander"),
                    createPokemonCard().copy(id = "sm3-3", name = "Charizard-GX", imageUrl = "https://images.pokemontcg.io/sm3/20.png", evolvesFrom = "Charmeleon")
            )

            adapter.setCards(SuperType.POKEMON, pokemons)
        }
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(DeckBuilderModule(this))
                .inject(this)
    }

    fun createPokemonCard(): PokemonCard {
        return PokemonCard("", "", null, "", "", null, SuperType.POKEMON, SubType.BASIC, null, null,
                null, ",", "", "", "", CardSet("", "", "", "", 0, false, false, "", ""), null, null,
                null, null)
    }


    companion object {
        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
    }
}