#!/usr/bin/env kscript


/*
 * Bulbapedia Info:
 * URL: https://bulbapedia.bulbagarden.net/wiki/Hidden_Fates_(TCG)
 *
 * Table Selector for list of card links:
 * #mw-content-text > table.multicol > tbody > tr > td:nth-child(1) > table:nth-child(4) > tbody > tr:nth-child(2) > td > table > tbody
 *
 * Shiny Vault collector card number selector:
 * tr > td:nth-child(1)
 *
 * Shiny Vault Card Link selector (href property, partial url):
 * tr > td:nth-child(3) > a
 *
 * Card Page
 *
 * Name/Expansion/Number Selector:
 * #firstHeading
 */

/*
 * Dependencies
 */

@file:DependsOn("org.jsoup:jsoup:1.11.3")
@file:DependsOn("io.pokemontcg:pokemon-tcg-sdk-kotlin:1.0.18")
@file:DependsOn("me.xdrop:fuzzywuzzy:1.1.10")
@file:DependsOn("com.google.code.gson:gson:2.8.5")
@file:DependsOn("com.squareup.okio:okio:2.4.0")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.1.1")
@file:MavenRepository("maven-central","http://central.maven.org/maven2/")

import DependsOn
import MavenRepository
import com.google.gson.GsonBuilder
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.pokemontcg.model.CardSet
import me.xdrop.fuzzywuzzy.FuzzySearch
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.File
import java.net.URL

/**
 * Class to store scraped info and to serialize to json
 */
data class OverrideCard(val id: String, val sourceId: String, val sourceSetCode: String)

val gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()

val pokemon = Pokemon(Config(logLevel = HttpLoggingInterceptor.Level.NONE))

println("Fetching all expansions...")
val shinyVaultSetPrefix = "sma"

// Get all the available expansions
val sets = pokemon.set().all()

println("Expansions fetched, scraping website")

val document = Jsoup.parse(URL("https://bulbapedia.bulbagarden.net/wiki/Hidden_Fates_(TCG)"), 30000)

val shinyVaultTable = document.select("#mw-content-text > table.multicol > tbody > tr > td:nth-child(1) > table:nth-child(4) > tbody > tr:nth-child(2) > td > table > tbody")
val shinyPokemon = shinyVaultTable.select("tr")

val shinyNumberRegex = "SV\\d+(?=\\/SV\\d+)".toRegex()
val pokemonNameSetNumberRegex = "\\w+ (?=(\\(([^)]+)\\)))".toRegex()

fun fuzzyMatchSet(name: String): CardSet? {
    return sets.find {
        if (name.contains("SM Promo") && it.code == "smp") {
            true
        } else {
            FuzzySearch.ratio(name, it.name) >= 98
        }
    }
}

val shinyMappings = shinyPokemon.subList(1, shinyPokemon.size - 1)
        .map { pokemon ->
            val number = shinyNumberRegex.find(pokemon.select("td:nth-child(1)").text())?.value?.trim() ?: "Unknown"
            val url = pokemon.select("td:nth-child(3) > a").first().absUrl("href")

            val sourceDocument = Jsoup.parse(URL(url), 30000)
            val sourceTitle = sourceDocument.select("#firstHeading").text().trim()

            val titleMatch = pokemonNameSetNumberRegex.find(sourceTitle)
            val name = titleMatch?.value ?: "Unknown"
            val setNumber = titleMatch?.groupValues?.get(2) ?: ""
            val sourceNumber = "\\d+".toRegex().find(setNumber)?.value
            val sourceExpansion = setNumber.replace("\\d+".toRegex(), "").trim()

//            println("Title [$sourceTitle]")
//            println("    Groups: ${titleMatch?.groupValues}")

            // Now find expansion
            var set = fuzzyMatchSet(sourceExpansion)
            if (set != null && set.series != "Sun & Moon") {
                // Shit, the site link to the first iteration of the card, now we need to query for it's newest generation that is HF
                println("Crap! Searching for newer set...")
                val setTables = pokemon.select("#mw-content-text > table:nth-child(1) > tbody > tr:nth-child(3) > td > table")
                val table = setTables.getOrNull(setTables.size - 3)
                if (table != null) {
                    val setTitle = table.select("tbody > tr:nth-child(1) > td:nth-child(2) > a").firstOrNull()?.text()
                    if (setTitle != null) {
                        set = fuzzyMatchSet(setTitle)
                    }
                }
            }

            println("Pokemon($name, id=$shinyVaultSetPrefix-$number) found Source(${set?.code}-${if (set?.code == "smp") "sm" else ""}$sourceNumber) for ($sourceExpansion - $sourceNumber)")

            OverrideCard(
                    "$shinyVaultSetPrefix-$number",
                    "${set?.code}-${if (set?.code == "smp") "sm" else ""}$sourceNumber",
                    set?.code ?: "Unknown"
            )
        }

val outputFile = File(File("."), "shinyOverrides.json")
if (outputFile.exists()) {
    outputFile.delete()
}
outputFile.createNewFile()

val overrideJson = gson.toJson(shinyMappings)

outputFile.outputStream().bufferedWriter()
        .use {
            it.write(overrideJson)
            it.flush()
        }

