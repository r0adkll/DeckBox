#!/usr/bin/env kscript

/*
 * Dependencies
 */
@file:DependsOn("io.pokemontcg:pokemon-tcg-sdk-kotlin:1.0.18")
@file:DependsOn("me.xdrop:fuzzywuzzy:1.1.10")
@file:DependsOn("com.google.code.gson:gson:2.8.5")

import com.google.gson.Gson
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.util.or
import me.xdrop.fuzzywuzzy.FuzzySearch
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

/*
 * Function and Class Definitions
 */

// Define hashing function
fun Card.reprintHash(): Long {
    return (this.name.hashCode().toLong() * 31L) +
        (this.text?.hashCode()?.toLong() ?: 0L * 31L)
}

/**
 * The output json class representation
 */
class Reprints(
    val standardHashes: List<Long>,
    val expandedHashes: List<Long>
)

/*
 * Script
 */

// Create our SDK instance
val pokemon = Pokemon(Config(logLevel = HttpLoggingInterceptor.Level.NONE))

println("Fetching all expansions...")

// Get all the available expansions
val sets = pokemon.set().all()

println("Fetching all trainer cards...")

// Query all the trainer cards on a per set basis and store them
val standardCards = ArrayList<Card>()
val expandedCards = ArrayList<Card>()
val unlimitedCards = ArrayList<Card>()
sets.forEach { set ->
    try {
        val cards = pokemon.card().where {
            setCode = set.code
            supertype = SuperType.TRAINER.displayName.or(SuperType.ENERGY.displayName)
        }.all().filter { it.supertype == SuperType.TRAINER || (it.supertype == SuperType.ENERGY && it.subtype == SubType.SPECIAL) }

        when {
            set.standardLegal -> standardCards += cards
            set.expandedLegal -> expandedCards += cards
            else -> unlimitedCards += cards
        }

        println("${set.name} cards fetched.")
    } catch (e: Exception) {
        println("ERRROR FETCHING: ${set.name}")
        e.printStackTrace()
    }
}

println("All trainer cards collected, Analyzing...")

// Define function that checks for reprints
fun checkReprints(legality: String, cards: List<Card>, constant: List<Card>): Pair<HashSet<String>, HashSet<Long>> {
    val reprints = HashSet<String>()
    val hashes = HashMap<Long, List<Card>>()
    cards.forEach { card ->
        // Search through the constant for a card with a matching name
        val matching = constant.find { it.name == card.name }
        if (matching != null) {
            // Fuzzy match card text
            val cardText = card.text?.joinToString(separator = " ") ?: ""
            val matchingText = matching.text?.joinToString(separator = " ") ?: ""
            val ratio = FuzzySearch.tokenSetPartialRatio(cardText, matchingText)
            if (ratio == 100) {
                println("$legality Match(${card.name}: (${card.id} => ${matching.id})): Fuzzy Ratio($ratio)")
                reprints += card.id

                val hash = card.reprintHash()
                val hashedCards = hashes.getOrDefault(hash, emptyList())
                hashes[hash] = hashedCards.plus(card)
            }
        }
    }

    // Print out hashse
    println()
    hashes.forEach { hash, hashedCards ->
        val isValidHash = hashedCards.all { it.name == hashedCards.firstOrNull()?.name }
        println("--= Hash: $hash, Valid: $isValidHash =--")
        println(hashedCards.joinToString(separator = "\n") { "• ${it.id} - ${it.name}" })
    }
    println()

    return reprints to hashes.keys.toHashSet()
}

// Now for both expanded and unlimited cards, compute and compare hash codes against standard
val standardReprints = checkReprints("Standard", expandedCards.plus(unlimitedCards), standardCards)
val expandedReprints = checkReprints("Expanded", unlimitedCards, expandedCards)

// Compile output text
val output =
    "Standard Reprint Hashes (#${standardReprints.second.size})\n" +
        "-----------------------\n\n" +
        standardReprints.second.joinToString(separator = "\n") { it.toString() } +
        "\n\n" +
        "Standard Reprint Ids (#${standardReprints.first.size})\n" +
        "-----------------------\n\n" +
        standardReprints.first.joinToString(separator = "\n") +
        "\n\n" +
        "Expanded Reprint Hashes (#${expandedReprints.second.size})\n" +
        "-----------------------\n\n" +
        expandedReprints.second.joinToString(separator = "\n") { it.toString() } +
        "\n\n" +
        "Expanded Reprint Ids (#${expandedReprints.first.size})\n" +
        "-----------------------\n\n" +
        expandedReprints.first.joinToString(separator = "\n")

val outputJsonObject = Reprints(standardReprints.second.toList(), expandedReprints.second.toList())

// generate files
val directory = File(".")
val reprintsFile = File(directory, "reprints.txt")
val reprintsJsonFile = File(directory, "reprints.json")

if (reprintsFile.exists()) {
    reprintsFile.delete()
}

if (reprintsJsonFile.exists()) {
    reprintsJsonFile.delete()
}

if (reprintsFile.createNewFile()) {
    reprintsFile.writeText(output)
    println("Reprint hash file generated @ ${reprintsFile.absolutePath}")
} else {
    println("ERROR: Unable to create reprint output file")
}

if (reprintsJsonFile.createNewFile()) {
    val gson = Gson()
    reprintsJsonFile.writeText(gson.toJson(outputJsonObject))
    println("Reprint hash json file generated @ ${reprintsFile.absolutePath}")
} else {
    println("ERROR: Unable to create reprint output json file")
}
