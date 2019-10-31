#!/usr/bin/env kscript

/*
Parsing card format

#post-225380 > section > div > blockquote
#post-226142 > section > div > blockquote:nth-child(5)

p >
    b | strong >
        a (href = hires image)
            > img (src = lowres image)
        text -> {Name}
    text -> " - {Type} - HP###"
    br
    text -> {Subtype} ?(TAG TEAM) ?- Evolves from {Name}
p >
    text -> "Ability: {Ability Name}"
    br
    text -> "{Ability Text}"
p >
    [{G|C|R|F|S|D|W|P|}]?+ {Attack Name}: ?(### damage). ?({Attack Text})
p? >
    text -> "Card text" // Typically the GX or TagTeam knock out text
p >
    text -> "Weakness: none|{Type} (x2)"
    br
    text -> "Resistance: none|{Type} (-20)"
    br
    text -> "Retreat: #"
hr

*/

@file:DependsOn("org.jsoup:jsoup:1.11.3")
@file:DependsOn("com.google.code.gson:gson:2.8.5")
@file:DependsOn("com.squareup.okio:okio:2.4.0")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.1.1")
@file:MavenRepository("maven-central","http://central.maven.org/maven2/")

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import okio.source
import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import org.jsoup.select.NodeVisitor
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

/*
 * Configuration
 */

if (args.size < 5) {
    println("No or insufficient arguments supplied")
    println("kscript previews.kts [URL] [setCode] [series] [set]")
    exitProcess(1)
}

// Get arguments
val pageUrl = args[0]     // i.e. https://pokebeach.com/...
val setCode = args[1]     // i.e. sm12 or sm115
val series = args[2]      // i.e. Sun & Moon
val set = args[3]         // i.e. Unified Minds
val outputPath = args[4]  // i.e. ~/Documents/Pokemon/sm12
val countOffset = args.getOrNull(5)?.toIntOrNull() ?: 0 // i.e. 34, used to offset the index counting
val domainUrl = "https://deckboxtcg.app"

println(
        """
            ||========================================================
            || Page URL: $pageUrl
            || Set Code: $setCode
            || Series: $series
            || Set: $set
            || Output: $outputPath
            || Count Offset: $countOffset
            ||========================================================
        """.trimIndent()
)

val gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()
val okHttpClient = OkHttpClient()

// Verify output path is directory
val outputDirectory = File(outputPath)
if (!outputDirectory.exists()) {
    println("Creating Output Directory (${outputDirectory.mkdirs()})")
} else {
    if (!outputDirectory.isDirectory) {
        println("Output path MUST be a directory")
        exitProcess(1)
    }
}

val imagesOutputDir = File(outputDirectory, "Images")
if (!imagesOutputDir.exists()) {
    imagesOutputDir.mkdirs()
}

val outputFile = File(outputDirectory, "$set.json")
if (outputFile.exists()) {
    outputFile.delete()
}
outputFile.createNewFile()

/*
 * Models
 */

fun downloadImage(url: String, name: String): String {
    val outputFile = File(imagesOutputDir, name)
    val call = okHttpClient.newCall(Request.Builder()
            .url(url)
            .get()
            .build())

    try {
        val response = call.execute()
        val output = outputFile.sink().buffer()
        response.body?.let { body ->
            output.writeAll(body.source())
        }
        output.flush()
        output.close()
    } catch (e: IOException) {
        println("Error getting URL $url")
        e.printStackTrace()
    }

    return ""
}

fun String.ext(): String {
    return this.substring(this.lastIndexOf('.'))
}

class Expansion(
        val code: String,
        val ptcgoCode: String,
        val name: String,
        val series: String,
        val totalCards: Int,
        val standardLegal: Boolean,
        val expandedLegal: Boolean,
        val releaseDate: String,
        val symbolUrl: String,
        val logoUrl: String
)

class Ability(
        val name: String,
        var text: String = "",
        val type: String? = "Ability"
)

class Attack(
        val cost: List<String>?,
        val name: String,
        val text: String?,
        val damage: String?,
        val convertedEnergyCost: Int
)

data class Effect(val type: String, val value: String)

data class Card(
        var id: String = "",
        var name: String = "",
        var nationalPokedexNumber: Int? = null,
        var imageUrl: String = "",
        var imageUrlHiRes: String = "",
        var types: List<String>? = null,
        var supertype: String = "",
        var subtype: String = "",
        var evolvesFrom: String? = null,
        var hp: String? = null,
        var retreatCost: List<String>? = null,
        var number: String = "",
        var artist: String? = null,
        var rarity: String? = null,
        var series: String = "",
        var set: String = "",
        var setCode: String = "",
        var text: MutableList<String>? = null,
        var attacks: MutableList<Attack>? = null,
        var weaknesses: List<Effect>? = null,
        var resistances: List<Effect>? = null,
        var ability: Ability? = null
)

/*
 * Nodes
 */

interface TagNode {

    fun head(node: Node): TagNode?
    fun tail(node: Node)
}

class EmptyNode : TagNode {
    override fun head(node: Node): TagNode? = null
    override fun tail(node: Node) = Unit
}

abstract class CardTagNode(protected val card: Card) : TagNode

inner class ImageNode(card: Card, node: Node) : CardTagNode(card) {

    init {
        val hiResImageUrl = node.attr("href")
        val outputName = card.number + "_hires${hiResImageUrl.ext()}"
        val hostingUrl = "$domainUrl/images/$setCode/$outputName"
        downloadImage(hiResImageUrl, outputName)
        card.imageUrlHiRes = hostingUrl
    }

    override fun head(node: Node): TagNode? {
        return when (node.nodeName()) {
            "img" -> {
                val imageUrl = node.attr("src")
                val outputName = card.number + imageUrl.ext()
                val hostingUrl = "$domainUrl/images/$setCode/$outputName"
                downloadImage(imageUrl, outputName)
                card.imageUrl = hostingUrl
                null
            }
            else -> null
        }
    }

    override fun tail(node: Node) {
    }
}

inner class HeaderNode(card: Card) : CardTagNode(card) {

    override fun head(node: Node): TagNode? {
        return when (node.nodeName()) {
            "a" -> ImageNode(card, node)
            "#text" -> {
                val text = node.attr("#text")
                card.name = text/*.replace("&amp;", "&")*/.trim()
                if (card.name.contains("GX")) {
                    card.subtype = "GX"
                }
                null
            }
            else -> null
        }
    }

    override fun tail(node: Node) {
    }
}

inner class PartNode(card: Card) : CardTagNode(card) {

    /**
     * See: [https://regex101.com/r/HFort6/1](https://regex101.com/r/HFort6/1)
     */
    val TYPE_REGEX = "(?! – )(Grass|Fire|Water|Psychic|Fighting|Darkness|Colorless|Dragon|Fairy|Lightning|Metal)(?= – HP\\d*)".toRegex()

    val HP_REGEX = "(?!HP)\\d.*".toRegex()

    val EVOLVES_REGEX = "(?<=Evolves from )\\w.*".toRegex()

    val ENERGY_REGEX = "^(\\[(C|D|N|Y|F|R|G|L|M|P|W)\\])+(\\+ |x )?".toRegex()
    val ENERGY_ITEM_REGEX = "\\[(C|D|N|Y|F|R|G|L|M|P|W)\\]".toRegex()

    val DAMAGE_AMOUNT_REGEX = "^\\d+(\\+|x)?(?!damage\\.)".toRegex()

    val DAMAGE_REGEX = "^\\d+(\\+|x)? damage\\.".toRegex()

    val WEAKNESS_REGEX = "(?<=Weakness: )(Grass|Fire|Water|Psychic|Fighting|Darkness|Colorless|Dragon|Fairy|Lightning|Metal)( x2)?".toRegex()
    val RESISTANCE_REGEX = "(?<=Resistance: )(Grass|Fire|Water|Psychic|Fighting|Darkness|Colorless|Dragon|Fairy|Lightning|Metal)( -20)?".toRegex()

    val RETREAT_REGEX = "(?<=Retreat: )\\d+".toRegex()

    fun String.type(): String? = when(this.toUpperCase()) {
        "C" -> "Colorless"
        "D" -> "Darkness"
        "N" -> "Dragon"
        "Y" -> "Fairy"
        "F" -> "Fighting"
        "R" -> "Fire"
        "G" -> "Grass"
        "L" -> "Lightning"
        "M" -> "Metal"
        "P" -> "Psychic"
        "W" -> "Water"
        else -> null
    }

    private var previousText: String? = null

    override fun head(node: Node): TagNode? {
        return when (node.nodeName()) {
            "a" -> ImageNode(card, node)
            "strong", "b" -> HeaderNode(card)
            "#text" -> {
                val text = node.attr("#text").trim()
                val typeMatch = TYPE_REGEX.find(text)

                //println("$text [[wasTyping] = ${previousText.wasTypingText()}]")

                // Check for Supertype
                when {
                    text.contains("– Trainer") -> card.supertype = "Trainer"
                    text.contains("– Special Energy") -> {
                        card.supertype = "Energy"
                        card.subtype = "Special"
                    }
                    typeMatch != null -> {
                        card.supertype = "Pokémon"
                        card.types = listOf(typeMatch.value)
                        card.hp = HP_REGEX.find(text)?.value
                    }
                    previousText.wasTypingText() -> {
                        if (text.contains("(TAG TEAM)")) {
                            card.subtype = "TAG TEAM"
                        } else if (card.subtype.isEmpty() && text.contains("Basic")) {
                            card.subtype = "Basic"
                        } else if (card.subtype.isEmpty() && text.contains("Stage 1")) {
                            card.subtype = "Stage 1"
                            card.evolvesFrom = EVOLVES_REGEX.find(text)?.value
                        } else if (card.subtype.isEmpty() && text.contains("Stage 2")) {
                            card.subtype = "Stage 2"
                            card.evolvesFrom = EVOLVES_REGEX.find(text)?.value
                        } else if (card.supertype == "Trainer") {
                            card.subtype = text
                        }
                    }
                    previousText?.startsWith("Ability:") == true -> {
                        card.ability?.text = text
                    }
                    else -> {
                        if (text.startsWith("Ability: ")) {
                            card.ability = Ability(text.replace("Ability:", "", true).trim())
                        } else if (ENERGY_REGEX.containsMatchIn(text)) {
                            val matches = ENERGY_ITEM_REGEX.findAll(text)
                            val energy = matches.map {
                                it.value.replace("[", "")
                                        .replace("]", "")
                                        .type()
                            }.filterNotNull().toList()

                            // Remove Energy from text
                            var attackText = text.replace(ENERGY_REGEX, "").trim()

                            // Get and remove name from text
                            val attackName = attackText.substring(0, attackText.indexOf(':')).trim()
                            attackText = attackText.substring(attackText.indexOf(':') + 1).trim()

                            // Get and remove damage from text
                            val attackDamage = DAMAGE_AMOUNT_REGEX.find(attackText)?.value
                            attackText = attackText.replace(DAMAGE_REGEX, "")

                            val actualAttackText = if (attackText.isBlank()) {
                                null
                            } else {
                                attackText
                            }
                            val attack = Attack(energy, attackName, actualAttackText, attackDamage, energy.size)

                            if (card.attacks == null) {
                                card.attacks = mutableListOf(attack)
                            } else {
                                card.attacks?.add(attack)
                            }
                        } else if (text.startsWith("Weakness:", true)) {
                            val weaknessType = WEAKNESS_REGEX.find(text)?.value
                            if (weaknessType != null) {
                                card.weaknesses = listOf(
                                        Effect(weaknessType, "×2")
                                )
                            }
                        } else if (text.startsWith("Resistance:", true)) {
                            val resistanceType = RESISTANCE_REGEX.find(text)?.value
                            if (resistanceType != null) {
                                card.resistances = listOf(
                                        Effect(resistanceType, "-20")
                                )
                            }
                        } else if (text.startsWith("Retreat:", true)) {
                            val retreatCount = RETREAT_REGEX.find(text)?.value?.toIntOrNull()
                            if (retreatCount != null) {
                                val retreatEnergy = (0 until retreatCount).map { "Colorless" }
                                card.retreatCost = retreatEnergy
                            }
                        } else {
                            if (card.text == null) {
                                card.text = mutableListOf(text)
                            } else {
                                card.text?.add(text)
                            }
                        }
                    }
                }

                previousText = text
                null
            }
            else -> null
        }
    }

    override fun tail(node: Node) {
    }

    private fun String?.wasTypingText(): Boolean {
        return this?.let {
            it.contains("– Trainer") ||
                    it.contains("– Special Energy") ||
                    TYPE_REGEX.containsMatchIn(it)
        } ?: false
    }
}

inner class BlockNode : TagNode {

    val cards = mutableListOf<Card>()
    var currentCard: Card = startNewCard()

    override fun head(node: Node): TagNode? {
        return when (node.nodeName()) {
            "p" -> PartNode(currentCard)
            "hr" -> {
                // We have reached the end of a card definition, add to
                println("Card Finished: ${currentCard.id}")

                // Make sure we only admin valid cards
                if (currentCard.name.isNotBlank()) {
                    cards += currentCard
                } else {
                    println(gson.toJson(currentCard))
                }
                currentCard = startNewCard()
                null
            }
            else -> null
        }
    }

    override fun tail(node: Node) {

    }

    private fun startNewCard(): Card {
        return Card(
                id = "$setCode-${cards.size + 1}",
                number = "${countOffset + cards.size + 1}",
                setCode = setCode,
                series = series,
                set = set
        )
    }
}

inner class TagNodeVisitor : NodeVisitor {

    private val nodeStack = ArrayDeque<TagNode>()

    override fun head(node: Node, depth: Int) {
        if (node.nodeName() == "blockquote") {
            nodeStack.push(BlockNode())
        } else {
            val newNode = nodeStack.peek().head(node) ?: EmptyNode()
            nodeStack.push(newNode)
        }
    }

    override fun tail(node: Node, depth: Int) {
        val tagNode = nodeStack.pop()
        tagNode.tail(node)

        if (tagNode is BlockNode) {
            tagNode.cards += tagNode.currentCard
            println("Finished parsing URL: $pageUrl")
            println("""
                ||==============================
                || $setCode
                || Total Cards (${tagNode.cards.size})
                || Expansion: $series - $set
                ||==============================
            """.trimIndent())
            val jsonString = gson.toJson(tagNode.cards)
            outputFile.sink().buffer().use {
                it.writeUtf8(jsonString)
                it.flush()
            }

            exitProcess(1)
        }
    }
}

/*
 * Scraping
 */

val url = URL(pageUrl)
println("Fetching: $url")
val document = Jsoup.parse(url, 30000)
val blockquote = document.select("#main-wrap > article > section > div > blockquote")
val encapsulatedSpans = blockquote.select("p > span")
encapsulatedSpans.forEach { span ->
    val parent = span.parent()
    println("[span, parent=(${parent.tagName()}, textNodes: ${parent.textNodes().size})] text=${span.text()}")

    if (parent.textNodes().isEmpty()) {
        span.textNodes().forEach {
            parent.appendText(it.text())
        }
    } else {
        // Combine the first text nodes of both parent and span, if there are more nodes in span, add them to parent text nodes
        val firstSpanTextNode = span.textNodes().firstOrNull()
        if (firstSpanTextNode != null) {
            parent.textNodes().first().apply {
                text(text() + firstSpanTextNode.text())
            }

            if (span.textNodes().size > 1) {
                span.textNodes().subList(1, span.textNodes().size).forEach {
                    parent.appendText(it.text())
                }
            }
        }
    }

    println("Appended Text Nodes:")
    println(span.parent().textNodes())

    span.remove()
}

fun printNodeTree(elements: Elements, name: String = "nodes.txt") {
    val scriptDir = File(".")
    val textFile = File(scriptDir, name)
    if (textFile.exists()) {
        textFile.delete()
    }
    textFile.createNewFile()
    textFile.sink().buffer().use { sink ->
        elements.traverse(object : NodeVisitor {
            override fun head(node: Node, depth: Int) {
                val txt = "${indent(depth)}[${node.nodeName()}] Attrs: ${node.attributes().toString().replace("\n", "")}"
                println(txt)
                sink.writeUtf8(txt).writeUtf8("\n")
            }

            override fun tail(node: Node, depth: Int) {
                val txt = "${indent(depth)}[/${node.nodeName()}]"
                println(txt)
                sink.writeUtf8(txt).writeUtf8("\n")
            }

            private fun indent(depth: Int): String {
                return (0 until depth).joinToString("") { " " }
            }
        })

        sink.flush()
        sink.close()
    }
}

// now we have the root container of the content to parse, begin 'stream' parsing
//printNodeTree(blockquote, "nodes_nospan.txt")
blockquote.traverse(TagNodeVisitor())
