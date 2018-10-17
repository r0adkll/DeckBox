#!/usr/bin/env kscript

/*
 * Dependencies
 */

@file:MavenRepository("maven-central","http://central.maven.org/maven2/")
@file:DependsOn("org.jsoup:jsoup:1.11.3")
@file:DependsOn("com.google.code.gson:gson:2.8.5")
@file:DependsOn("io.pokemontcg:pokemon-tcg-sdk-kotlin:1.0.15")

@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/decks/model/AttackEntity.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/decks/model/DeckEntity.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/decks/model/PokemonCardEntity.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/importer/model/CardSpec.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/importer/parser/LineValidator.kt")

/*
 * Imports
 */

import com.google.gson.Gson
import org.jsoup.Jsoup
import java.io.File
import java.net.URL
import kotlin.math.min


/*
 * Data Models
 */

data class Tournament(
        val name: String,
        val date: String,
        val country: String,
        val format: String,
        val playerCount: Int,
        val players: List<Player>
)

data class Player(
        val place: Int,
        val name: String,
        val deckList: String
)


/*
 * Functions
 */

fun loadDecklistsForTournament(url: String, count: Int): List<Player> {
    val players = ArrayList<Player>()
    val document = Jsoup.parse(URL("$url&show=lists"), 30000)
    val decklists = document.select("h3.tournamentdecklists-heading, div.decklist")
    if (decklists.isNotEmpty()) {

        var currentRank: Int? = null
        var currentName: String? = null

        val minCount = min(count * 2, decklists.size)
        println("Loading ($count :: ${decklists.size}) player decks...")
        (0 until minCount).forEach {
            val element = decklists[it]
            if (element.`is`("h3.tournamentdecklists-heading")) {
                val parts = element.text().split("-")

                // rank
                currentRank = parts[0].trim().replace("st", "").replace("nd", "").replace("rd", "").replace("th", "").toIntOrNull() ?: -1
                currentName = parts[1].trim()

                println("Player: #$currentRank - $currentName")
            } else if (element.`is`("div.decklist")) {

                val decklist = element.select("form > input").attr("value")
                players += Player(currentRank ?: -1, currentName ?: "Unknown", decklist)

                println("Player: #$currentRank - $currentName - Decklist")
            }
        }
    }

    return players
}


/*
 * Scrape the list of tournaments and decklists
 */

val tournaments = ArrayList<Tournament>()

val tournamentsUrl = URL("http://limitlesstcg.com/tournaments/")
val tournamentsDocument = Jsoup.parse(tournamentsUrl, 30000)
val rankingTable = tournamentsDocument.select("div#complete > table.rankingtable > tbody > tr")

rankingTable.forEach { row ->
    val columns = row.select("td")
    if (columns.isNotEmpty()) {

        // Date Column
        val date = columns[0].text()

        // Country Column
        val country = columns[1].child(0).child(0).attr("alt")

        // Name Column
        val name = columns[2].child(0).text()

        // URL
        val url = columns[2].child(0).absUrl("href")

        // Format Column
        val format = columns[3].child(0).child(0).attr("alt")

        // Player Count Column
        val playerCount = columns[4].text().toIntOrNull() ?: -1

        println("Tournament Found ($date, $country, $name, $format, $playerCount)")

        // Create tournament object
        tournaments += Tournament(name, date, country, format, playerCount, loadDecklistsForTournament(url, 8))
    }
}

val gson = Gson()
val scriptDir = File(".")
val outputFile = File(scriptDir, "tournaments.json")

if (outputFile.exists()) {
    outputFile.delete()
}

val json = gson.toJson(tournaments.filter { it.format == "Standard" || it.format == "Expanded" })
outputFile.writeText(json)