package com.r0adkll.deckbuilder.tools

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType


object ModelUtils {

    val EXPANSIONS = arrayListOf(
            Expansion("base1", "BS", "Base", "", 0, true, true, "", "", null),
            Expansion("base2", "JU", "Jungle", "", 0, true, true, "", "", null),
            Expansion("basep", "PR", "Wizards Black Star Promos", "", 0, true, true, "", "", null),
            Expansion("base3", "FO", "Fossil", "", 0, true, true, "", "", null),
            Expansion("base4", "B2", "Base Set 2", "", 0, true, true, "", "", null),
            Expansion("base5", "TR", "Team Rocket", "", 0, true, true, "", "", null),
            Expansion("gym1", "G1", "Gym Heroes", "", 0, true, true, "", "", null),
            Expansion("gym2", "G2", "Gym Challenge", "", 0, true, true, "", "", null),
            Expansion("neo1", "N1", "Neo Genesis", "", 0, true, true, "", "", null),
            Expansion("neo2", "N2", "Neo Discovery", "", 0, true, true, "", "", null),
            Expansion("neo3", "N3", "Neo Revelation", "", 0, true, true, "", "", null),
            Expansion("neo4", "N4", "Neo Destiny", "", 0, true, true, "", "", null),
            Expansion("base6", "LC", "Legendary Collection", "", 0, true, true, "", "", null),
            Expansion("ecard1", "EX", "Expedition Base Set", "", 0, true, true, "", "", null),
            Expansion("ecard2", "AQ", "Aquapolis", "", 0, true, true, "", "", null),
            Expansion("ecard3", "SK", "Skyridge", "", 0, true, true, "", "", null),
            Expansion("ex1", "RS", "Ruby & Sapphire", "", 0, true, true, "", "", null),
            Expansion("ex3", "DR", "Dragon", "", 0, true, true, "", "", null),
            Expansion("ex2", "SS", "Sandstorm", "", 0, true, true, "", "", null),
            Expansion("ex4", "MA", "Team Magma vs Team Aqua", "", 0, true, true, "", "", null),
            Expansion("ex5", "HL", "Hidden Legends", "", 0, true, true, "", "", null),
            Expansion("pop1", "null", "POP Series 1", "", 0, true, true, "", "", null),
            Expansion("ex6", "RG", "FireRed & LeafGreen", "", 0, true, true, "", "", null),
            Expansion("ex7", "RR", "Team Rocket Returns", "", 0, true, true, "", "", null),
            Expansion("ex8", "DX", "Deoxys", "", 0, true, true, "", "", null),
            Expansion("ex9", "EM", "Emerald", "", 0, true, true, "", "", null),
            Expansion("pop2", "null", "POP Series 2", "", 0, true, true, "", "", null),
            Expansion("ex10", "UF", "Unseen Forces", "", 0, true, true, "", "", null),
            Expansion("ex11", "DS", "Delta Species", "", 0, true, true, "", "", null),
            Expansion("ex12", "LM", "Legend Maker", "", 0, true, true, "", "", null),
            Expansion("pop3", "null", "POP Series 3", "", 0, true, true, "", "", null),
            Expansion("ex13", "HP", "Holon Phantoms", "", 0, true, true, "", "", null),
            Expansion("ex14", "CG", "Crystal Guardians", "", 0, true, true, "", "", null),
            Expansion("pop4", "null", "POP Series 4", "", 0, true, true, "", "", null),
            Expansion("ex15", "DF", "Dragon Frontiers", "", 0, true, true, "", "", null),
            Expansion("ex16", "PK", "Power Keepers", "", 0, true, true, "", "", null),
            Expansion("pop5", "null", "POP Series 5", "", 0, true, true, "", "", null),
            Expansion("dp1", "DP", "Diamond & Pearl", "", 0, true, true, "", "", null),
            Expansion("dp2", "MT", "Mysterious Treasures", "", 0, true, true, "", "", null),
            Expansion("pop6", "null", "POP Series 6", "", 0, true, true, "", "", null),
            Expansion("dp3", "SW", "Secret Wonders", "", 0, true, true, "", "", null),
            Expansion("dp4", "GE", "Great Encounters", "", 0, true, true, "", "", null),
            Expansion("pop7", "null", "POP Series 7", "", 0, true, true, "", "", null),
            Expansion("dp5", "MD", "Majestic Dawn", "", 0, true, true, "", "", null),
            Expansion("dp6", "LA", "Legends Awakened", "", 0, true, true, "", "", null),
            Expansion("pop8", "null", "POP Series 8", "", 0, true, true, "", "", null),
            Expansion("dp7", "SF", "Stormfront", "", 0, true, true, "", "", null),
            Expansion("pl1", "PL", "Platinum", "", 0, true, true, "", "", null),
            Expansion("pop9", "null", "POP Series 9", "", 0, true, true, "", "", null),
            Expansion("pl2", "RR", "Rising Rivals", "", 0, true, true, "", "", null),
            Expansion("pl3", "SV", "Supreme Victors", "", 0, true, true, "", "", null),
            Expansion("pl4", "AR", "Arceus", "", 0, true, true, "", "", null),
            Expansion("hgss1", "HS", "HeartGold & SoulSilver", "", 0, true, true, "", "", null),
            Expansion("hgss2", "UL", "HS—Unleashed", "", 0, true, true, "", "", null),
            Expansion("hgss3", "UD", "HS—Undaunted", "", 0, true, true, "", "", null),
            Expansion("hgss4", "TM", "HS—Triumphant", "", 0, true, true, "", "", null),
            Expansion("col1", "CL", "Call of Legends", "", 0, true, true, "", "", null),
            Expansion("bwp", "PR-BLW", "BW Black Star Promos", "", 0, true, true, "", "", null),
            Expansion("bw1", "BLW", "Black & White", "", 0, true, true, "", "", null),
            Expansion("bw2", "EPO", "Emerging Powers", "", 0, true, true, "", "", null),
            Expansion("bw3", "NVI", "Noble Victories", "", 0, true, true, "", "", null),
            Expansion("bw4", "NXD", "Next Destinies", "", 0, true, true, "", "", null),
            Expansion("bw5", "DEX", "Dark Explorers", "", 0, true, true, "", "", null),
            Expansion("bw6", "DRX", "Dragons Exalted", "", 0, true, true, "", "", null),
            Expansion("dv1", "DRV", "Dragon Vault", "", 0, true, true, "", "", null),
            Expansion("bw7", "BCR", "Boundaries Crossed", "", 0, true, true, "", "", null),
            Expansion("bw8", "PLS", "Plasma Storm", "", 0, true, true, "", "", null),
            Expansion("bw9", "PLF", "Plasma Freeze", "", 0, true, true, "", "", null),
            Expansion("bw10", "PLB", "Plasma Blast", "", 0, true, true, "", "", null),
            Expansion("xyp", "PR-XY", "XY Black Star Promos", "", 0, true, true, "", "", null),
            Expansion("bw11", "LTR", "Legendary Treasures", "", 0, true, true, "", "", null),
            Expansion("xy0", "KSS", "Kalos Starter Set", "", 0, true, true, "", "", null),
            Expansion("xy1", "XY", "XY", "", 0, true, true, "", "", null),
            Expansion("xy2", "FLF", "Flashfire", "", 0, true, true, "", "", null),
            Expansion("xy3", "FFI", "Furious Fists", "", 0, true, true, "", "", null),
            Expansion("xy4", "PHF", "Phantom Forces", "", 0, true, true, "", "", null),
            Expansion("xy5", "PRC", "Primal Clash", "", 0, true, true, "", "", null),
            Expansion("dc1", "DCR", "Double Crisis", "", 0, true, true, "", "", null),
            Expansion("xy6", "ROS", "Roaring Skies", "", 0, true, true, "", "", null),
            Expansion("xy7", "AOR", "Ancient Origins", "", 0, true, true, "", "", null),
            Expansion("xy8", "BKT", "BREAKthrough", "", 0, true, true, "", "", null),
            Expansion("xy9", "BKP", "BREAKpoint", "", 0, true, true, "", "", null),
            Expansion("g1", "GEN", "Generations", "", 0, true, true, "", "", null),
            Expansion("xy10", "FCO", "Fates Collide", "", 0, true, true, "", "", null),
            Expansion("xy11", "STS", "Steam Siege", "", 0, true, true, "", "", null),
            Expansion("xy12", "EVO", "Evolutions", "", 0, true, true, "", "", null),
            Expansion("smp", "PR-SM", "SM Black Star Promos", "", 0, true, true, "", "", null),
            Expansion("sm1", "SUM", "Sun & Moon", "", 0, true, true, "", "", null),
            Expansion("sm2", "GRI", "Guardians Rising", "", 0, true, true, "", "", null),
            Expansion("sm3", "BUS", "Burning Shadows", "", 0, true, true, "", "", null),
            Expansion("sm35", "SLG", "Shining Legends", "", 0, true, true, "", "", null),
            Expansion("sm4", "CIN", "Crimson Invasion", "", 0, true, true, "", "", null),
            Expansion("sm5", "UPR", "Ultra Prism", "", 0, true, true, "", "", null),
            Expansion("sm6", "FLI", "Forbidden Light", "", 0, true, true, "", "", null),
            Expansion("sm7", "CES", "Celestial Storm", "", 0, true, true, "", "", null)
    )

    fun createPokemonCard(name: String = ""): PokemonCard {
        return PokemonCard("", name, null, "", "", null, SuperType.POKEMON, SubType.BASIC, null, null,
                null, ",", "", "", "", Expansion("", "", "", "", 0, false, false, "", "", ""), null, null,
                null, null, null)
    }

    fun createEnergyCard(name: String = ""): PokemonCard {
        return createPokemonCard(name).copy(supertype = SuperType.ENERGY, subtype = SubType.BASIC)
    }

    fun createStackedPokemonCard(count: Int = 1): StackedPokemonCard {
        return StackedPokemonCard(createPokemonCard(), count)
    }

    fun createExpansion(
            code: String,
            ptcgoCode: String? = null,
            name: String = "",
            series: String = "",
            totalCards: Int = 0,
            standardLegal: Boolean = false,
            expandedLegal: Boolean = false,
            releaseDate: String = "",
            symbolUrl: String = "",
            logoUrl: String? = null
    ): Expansion = Expansion(code, ptcgoCode, name, series, totalCards, standardLegal,
            expandedLegal, releaseDate, symbolUrl, logoUrl)
}