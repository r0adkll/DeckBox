package com.r0adkll.deckbuilder.arch.data.features.importer.parser

/**
 * Interface for validating a line of text in a deck list for importing
 */
class LineValidator {

    /**
     * Validate a line of a deck list to see if it can be parsed into a CardSpec
     * @return a raw valid card spec string that can be imported using the apps format, or null if just plain invalid
     */
    fun validate(line: String): String? {

        // Walk the string by character until we encounter a digit, then trim previous characters
        line.forEachIndexed { index, c ->
            if (c.isDigit()) {
                val cleanLine = line.substring(index)

                // Now validate that our clean line has the correct number of parse, and the correct format
                val parts = cleanLine.trim().split(" ")

                // Validate count
                if (parts.size < MIN_PART_COUNT) {
                    return null // Invalid formatted line
                }

                // Validate card count
                parts[0].toIntOrNull() ?: return null

                return cleanLine
            }
        }

        return null
    }

    companion object {
        private const val MIN_PART_COUNT = 4
    }
}
