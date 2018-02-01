package com.r0adkll.deckbuilder.arch.domain.features.ptcgo.model

import io.pokemontcg.model.Type
import io.pokemontcg.model.Type.*


sealed class BasicEnergySet {

    abstract fun convert(type: Type): String?

    object SunMoon : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "sm1-164"
            FIRE -> "sm1-165"
            WATER -> "sm1-166"
            LIGHTNING -> "sm1-167"
            PSYCHIC -> "sm1-168"
            FIGHTING -> "sm1-169"
            DARKNESS -> "sm1-170"
            METAL -> "sm1-171"
            FAIRY -> "sm1-172"
            else -> null
        }
    }

    object XY : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "xy1-132"
            FIRE -> "xy1-133"
            WATER -> "xy1-134"
            LIGHTNING -> "xy1-135"
            PSYCHIC -> "xy1-136"
            FIGHTING -> "xy1-137"
            DARKNESS -> "xy1-138"
            METAL -> "xy1-139"
            FAIRY -> "xy1-140"
            else -> null
        }
    }


    object Generations : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "g1-75"
            FIRE -> "g1-76"
            WATER -> "g1-77"
            LIGHTNING -> "g1-78"
            PSYCHIC -> "g1-79"
            FIGHTING -> "g1-80"
            DARKNESS -> "g1-81"
            METAL -> "g1-82"
            FAIRY -> "g1-83"
            else -> null
        }
    }


    object Evolutions : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "xy12-91"
            FIRE -> "xy12-92"
            WATER -> "xy12-93"
            LIGHTNING -> "xy12-94"
            PSYCHIC -> "xy12-95"
            FIGHTING -> "xy12-96"
            DARKNESS -> "xy12-97"
            METAL -> "xy12-98"
            FAIRY -> "xy12-99"
            else -> null
        }
    }


    object ECard : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "ecard1-162"
            FIRE -> "ecard1-161"
            WATER -> "ecard1-165"
            LIGHTNING -> "ecard1-163"
            PSYCHIC -> "ecard1-164"
            FIGHTING -> "ecard1-160"
            else -> null
        }
    }


    object Emerald : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "ex9-101"
            FIRE -> "ex9-102"
            WATER -> "ex9-103"
            LIGHTNING -> "ex9-104"
            PSYCHIC -> "ex9-105"
            FIGHTING -> "ex9-106"
            else -> null
        }
    }


    object Legends : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "col1-88"
            FIRE -> "col1-89"
            WATER -> "col1-90"
            LIGHTNING -> "col1-91"
            PSYCHIC -> "col1-92"
            FIGHTING -> "col1-93"
            DARKNESS -> "col1-94"
            METAL -> "col1-95"
            else -> null
        }
    }


    object HGSS : BasicEnergySet() {
        override fun convert(type: Type): String? = when(type) {
            GRASS -> "hgss1-115"
            FIRE -> "hgss1-116"
            WATER -> "hgss1-117"
            LIGHTNING -> "hgss1-118"
            PSYCHIC -> "hgss1-119"
            FIGHTING -> "hgss1-120"
            DARKNESS -> "hgss1-121"
            METAL -> "hgss1-122"
            else -> null
        }
    }


}