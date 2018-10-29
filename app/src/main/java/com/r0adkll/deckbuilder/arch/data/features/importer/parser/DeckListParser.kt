package com.r0adkll.deckbuilder.arch.data.features.importer.parser

import com.r0adkll.deckbuilder.arch.data.features.importer.model.CardSpec
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import java.io.StringReader


class DeckListParser {

    private val validator = LineValidator()


    fun parse(expansions: List<Expansion>, deckList: String): List<CardSpec> {
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