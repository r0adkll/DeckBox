package com.r0adkll.deckbuilder.util.extensions

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.*
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


fun Iterable<PokemonCard>.isMulligan(): Boolean {
    return this.none {
        it.supertype == SuperType.POKEMON
                && (it.subtype == SubType.BASIC || it.evolvesFrom.isNullOrBlank())
    }
}

@DrawableRes
fun Type.drawable(): Int = when(this) {
    Type.COLORLESS -> R.drawable.ic_poketype_colorless
    Type.FIRE -> R.drawable.ic_poketype_fire
    Type.GRASS -> R.drawable.ic_poketype_grass
    Type.WATER -> R.drawable.ic_poketype_water
    Type.LIGHTNING -> R.drawable.ic_poketype_electric
    Type.FIGHTING -> R.drawable.ic_poketype_fighting
    Type.PSYCHIC -> R.drawable.ic_poketype_psychic
    Type.METAL -> R.drawable.ic_poketype_steel
    Type.DRAGON -> R.drawable.ic_poketype_dragon
    Type.FAIRY -> R.drawable.ic_poketype_fairy
    Type.DARKNESS -> R.drawable.ic_poketype_dark
    else -> R.drawable.ic_poketype_colorless
}


@ColorRes
fun Type.color(): Int = when(this) {
    Type.COLORLESS -> R.color.poketype_colorless
    Type.FIRE -> R.color.poketype_fire
    Type.GRASS -> R.color.poketype_grass
    Type.WATER -> R.color.poketype_water
    Type.LIGHTNING -> R.color.poketype_electric
    Type.FIGHTING -> R.color.poketype_fighting
    Type.PSYCHIC -> R.color.poketype_psychic
    Type.METAL -> R.color.poketype_steel
    Type.DRAGON -> R.color.poketype_dragon
    Type.FAIRY -> R.color.poketype_fairy
    Type.DARKNESS -> R.color.poketype_dark
    else -> R.color.poketype_colorless
}


class PokemonCardBuilder(
        var id: String = "",
        var name: String = "",
        var nationalPokedexNumber: Int? = null,
        var imageUrl: String = "",
        var imageUrlHiRes: String = "",
        var types: List<Type>? = null,
        var supertype: SuperType = SuperType.POKEMON,
        var subtype: SubType = SubType.UNKNOWN,
        var evolvesFrom: String? = null,
        var hp: Int? = null,
        var retreatCost: List<Type>? = null,
        var number: String = "",
        var artist: String = "",
        var rarity: String? = null,
        var series: String = "",
        var expansion: Expansion? = null,
        var text: List<String>? = null,
        var attacks: List<Attack>? = null,
        var weaknesses: List<Effect>? = null,
        var resistances: List<Effect>? = null,
        var ability: Ability? = null,
        var isCached: Boolean = false
) {

    fun build(): PokemonCard = PokemonCard(
            id, name, nationalPokedexNumber, imageUrl, imageUrlHiRes, types, supertype, subtype,
            evolvesFrom, hp, retreatCost, number, artist, rarity, series, expansion, text, attacks,
            weaknesses, resistances, ability, isCached
    )
}


fun pokemon(builder: PokemonCardBuilder.() -> Unit): PokemonCard {
    val b = PokemonCardBuilder()
    builder.invoke(b)
    return b.build()
}