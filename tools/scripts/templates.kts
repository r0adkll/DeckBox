#!/usr/bin/env kscript

/*
 * Dependencies
 */

@file:DependsOn("org.jsoup:jsoup:1.11.3")
@file:DependsOn("com.google.code.gson:gson:2.8.5")
@file:DependsOn("io.pokemontcg:pokemon-tcg-sdk-kotlin:1.0.19")
@file:DependsOn("com.google.firebase:firebase-admin:6.5.0")
@file:MavenRepository("maven-central","http://central.maven.org/maven2/")

@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/decks/model/CardMetadataEntity.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/importer/model/CardSpec.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/importer/parser/LineValidator.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/domain/features/importer/model/BasicEnergySet.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/community/model/TournamentEntity.kt")
@file:Include("../../app/src/main/java/com/r0adkll/deckbuilder/arch/data/features/community/model/DeckInfoEntity.kt")

import DependsOn
import MavenRepository
import Include
import com.google.api.client.util.ArrayMap
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient

/*
 * Imports
 */

import com.google.gson.Gson
//import com.r0adkll.deckbuilder.arch.data.features.community.model.DeckInfoEntity
//import com.r0adkll.deckbuilder.arch.data.features.decks.model.CardMetadataEntity
//import com.r0adkll.deckbuilder.arch.data.features.community.model.TournamentEntity
//import com.r0adkll.deckbuilder.arch.data.features.importer.model.CardSpec
//import com.r0adkll.deckbuilder.arch.data.features.importer.parser.LineValidator
//import com.r0adkll.deckbuilder.arch.domain.features.importer.model.BasicEnergySet
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.model.CardSet
import io.pokemontcg.model.Effect
import io.pokemontcg.model.Type
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.io.StringReader
import java.net.URL
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.min
import kotlin.system.exitProcess

/*
 * Configuration
 */

if (args.isEmpty()) {
    println("No arguments supplied")
    exitProcess(1)
}

val firebaseConfigPath = args[0]
val serviceAccount = FileInputStream(firebaseConfigPath)
val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://deck-builder-1b711.firebaseio.com/")
        .build()
FirebaseApp.initializeApp(options)

val pokemon = Pokemon(Config(logLevel = HttpLoggingInterceptor.Level.NONE))

println("Loading all expansions...")
val expansions = pokemon.set().all()
val parser = DeckListParser()
val basicEnergySet = BasicEnergySet.SunMoon

/*
 * Data Models
 */

class TournamentDeckTemplateEntity(
        val rank: Int = 0,
        val name: String = "",
        val description: String = "",
        val image: String? = null,
        val author: String = "",
        val authorCountry: String = "",
        val deckInfo: List<DeckInfoEntity> = emptyList(),
        val cardMetadata: List<CardMetadataEntity>? = null,
        val tournament: TournamentEntity? = null,
        val timestamp: Long = 0L
)

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
        val country: String,
        val deckInfo: List<DeckInfoEntity>,
        val deckList: String
)

class DeckListParser {
    private val validator = LineValidator()

    fun parse(expansions: List<CardSet>, deckList: String): List<CardSpec> {
        val reader = StringReader(deckList)
        val lines = reader.readLines()

        val cards = ArrayList<CardSpec>()
        lines.forEach {
            val validatedLine = validator.validate(it)
            if (validatedLine != null) {
                val count = parseCount(validatedLine)
                if (count != null) {
                    val setInfo = parseSetInformation(validatedLine)
                    val name = parseName(validatedLine)
                    val expansion = expansions.find { it.ptcgoCode == setInfo.first }
                    val id = "${expansion?.code}-${setInfo.second}"
                    cards.add(CardSpec(count, name, setInfo.first, setInfo.second, id))
                }
            }
        }

        return cards
    }

    private fun parseCount(line: String): Int? {
        val parts = line.trim().split(" ")
        val count = parts.firstOrNull()
        return count?.toIntOrNull()
    }

    private fun parseSetInformation(line: String): Pair<String, String> {
        val parts = line.trim().split(" ")
        val number = parts.last()
        val set = parts[parts.size - 2]
        return Pair(set, number)
    }

    private fun parseName(line: String): String {
        val parts = line.trim().split(" ").toMutableList()
        val nameParts = parts
                .drop(1) // Drop the card count
                .dropLast(2) // Drop the set information
        return nameParts.joinToString(" ")
    }
}

/*
 * Functions
 */

fun loadTournamentWinners(url: String, count: Int): List<Player> {
    val players = ArrayList<Player>()

    val document = Jsoup.parse(URL(url), 30000)
    val playerList = document.select("table.rankingtable > tbody > tr")
    if (playerList.isNotEmpty()) {
        val c = Math.min(playerList.size, count)
        (0 until c).forEach {
            val row = playerList[it]
            val columns = row.select("td")
            if (columns.isNotEmpty()) {

                // Get Rank
                val rank = columns[0].text().toIntOrNull() ?: 0

                // Get Name
                val name = columns[1].child(0).text()

                // Get Country
                val country = columns[2].child(0).child(0).attr("alt")

                // Get Deck Info
                val infos = columns[3].select("span > a > img").map {
                    DeckInfoEntity(it.attr("alt"), it.absUrl("src"))
                }

                val deckListUrl = columns[4].select("a").firstOrNull()?.absUrl("href")

                if (deckListUrl != null) {
                    val deckListDocument = Jsoup.parse(URL(deckListUrl), 30000)
                    val deckList = deckListDocument.select("div.decklist > div.list-modal > textarea").firstOrNull()?.text()
                    if (deckList != null) {
                        players += Player(rank, name, country, infos, deckList)
                    }
                }
            }
        }

    }

    return players
}

fun find(ids: List<String>): List<Card> {
    return pokemon.card()
            .where {
                id = ids.joinToString("|")
            }
            .all()
}

fun filterEnergy(): (CardSpec) -> Boolean {
    return { spec ->
        Type.VALUES.filter { it != Type.COLORLESS && it != Type.UNKNOWN && it != Type.DRAGON }
                .find {
                    spec.name.contains("${it.name} Energy", true) ||
                            (spec.name.contains(it.name, true) && spec.set.contains("Energy", true))
                } != null
    }
}

fun mapEnergy(): (CardSpec) -> Pair<CardSpec, String>? {
    return { spec ->
        val type = Type.VALUES.filter { it != Type.COLORLESS && it != Type.UNKNOWN && it != Type.DRAGON }
                .find {
                    spec.name.contains("${it.name} Energy", true) ||
                            (spec.name.contains(it.name, true) && spec.set.contains("Energy", true))
                }
        type?.let {
            basicEnergySet.convert(it)?.let { Pair(spec, it) }
        }
    }
}

fun Type.compact(): String = when(this) {
    Type.COLORLESS -> "C"
    Type.DARKNESS -> "D"
    Type.DRAGON -> "N"
    Type.FAIRY -> "Y"
    Type.FIGHTING -> "F"
    Type.FIRE -> "R"
    Type.GRASS -> "G"
    Type.LIGHTNING -> "L"
    Type.METAL -> "M"
    Type.PSYCHIC -> "P"
    Type.WATER -> "W"
    Type.UNKNOWN -> ""
}

fun List<Type>.compactTypes(): String {
    return this.fold("") { acc, type ->
        acc.plus(type.compact())
    }
}

fun List<Effect>.compactEffects(): String {
    return this.foldIndexed("") { index, acc, effect ->
        val new = acc.plus("[${effect.type.displayName[0].toUpperCase()}|${effect.value}]")
        if (index != this.size - 1) {
            new.plus(",")
        } else {
            new
        }
    }
}

fun List<Card>.stackCards(): List<Pair<Card, Int>> {
    val map = ArrayMap<Card, Int>()
    this.forEach { card ->
        val count = map[card] ?: 0
        map[card] = count + 1
    }
    return map.map { Pair(it.key, it.value) }
}

fun Pair<Card, Int>.entity(): CardMetadataEntity = CardMetadataEntity(
        this.first.id,
        this.first.supertype.displayName,
        this.first.imageUrl,
        this.first.imageUrlHiRes,
        this.second
)

fun importDeckList(deckList: String): List<CardMetadataEntity> {
    val allCards = ArrayList<Card>()

    val cards = parser.parse(expansions, deckList)
    val ids = cards.map { it.id }

    val foundPokemon = find(ids)
    allCards += foundPokemon.flatMap { poke ->
        val count = cards.find { it.id == poke.id }?.count ?: 0
        (0 until count).map { poke.copy()}
    }

    val expectedCount = cards.sumBy { it.count }
    if (expectedCount != allCards.size) {
        println("Uh-oh! It looks like we haven't imported all of the cards!")

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

            println("Searching for default energy: $missingEnergy")

            val foundEnergyCards = find(energyIds)
            allCards += foundEnergyCards.flatMap { poke ->
                val count = missingEnergyCards.find { it.second == poke.id }?.first?.count ?: 0
                println("* $count Energy($poke)")
                (0 until count).map { poke.copy() }
            }

            return allCards.stackCards().map { it.entity() }
        } else {
            return allCards.stackCards().map { it.entity() }
        }
    } else {
        return allCards.stackCards().map { it.entity() }
    }
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
        tournaments += Tournament(name, date, country, format, playerCount, loadTournamentWinners(url, 8))
    }
}

/*
 * Parse and import tournament decks into Firebase template models
 */

@Suppress("ConvertCallChainIntoSequence")
val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yy")
val tournamentDeckTemplates = tournaments
        .filter { it.players.isNotEmpty() }
        .filter { it.format == "Standard" || it.format == "Expanded" }
        .flatMap { tournament ->
            tournament.players.map { player ->
                // Parse decklist
                val deckName = "${tournament.name} - Winner"
                val deckListCards = importDeckList(player.deckList)
                val dateTime = LocalDate.parse(tournament.date, dateFormatter)
                TournamentDeckTemplateEntity(
                        player.place,
                        deckName,
                        "",
                        null,
                        player.name,
                        player.country,
                        player.deckInfo,
                        deckListCards,
                        TournamentEntity(tournament.name, tournament.date, tournament.country, tournament.format, tournament.playerCount),
                        dateTime.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000L
                )
            }
        }

val db = FirestoreClient.getFirestore()
val collection =
        db.collection("templates")
        .document("tournaments")
        .collection("decks")

val batch = db.batch()

tournamentDeckTemplates.forEach { template ->
    val key = "${template.tournament!!.name}-${template.tournament.date}-${template.rank}-${template.author}"
    val document = collection.document(key)
    batch.set(document, template)
}

val results = batch.commit().get()
println("Templates uploaded to Firestore: $results")

println("Generating JSON file...")
val gson = Gson()
val scriptDir = File(".")
val outputFile = File(scriptDir, "tournaments.json")

if (outputFile.exists()) {
    outputFile.delete()
}

val json = gson.toJson(tournamentDeckTemplates)
outputFile.writeText(json)
println("====== Finished =====")
