package app.deckbox.tournament.xml.parsers

import app.deckbox.core.logging.bark
import app.deckbox.tournament.xml.SoupParser
import app.deckbox.tournament.xml.builders.ParticipantBuilder
import app.deckbox.tournament.xml.builders.ParticipantListBuilder

class ParticipantListParser(
  private val participantListBuilder: ParticipantListBuilder,
) : SoupParser() {

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    if (name == "table" && attributes["class"]?.contains("data-table striped") == true) {
      // We've found the table of participants, push a parser to handle those
      push(ParticipantParser(participantListBuilder))
    }
  }
}

private class ParticipantParser(
  private val participantListBuilder: ParticipantListBuilder,
) : SoupParser() {

  private var currentParticipantBuilder: ParticipantBuilder? = null

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    when (name) {
      "tr" -> {
        currentParticipantBuilder = ParticipantBuilder()
      }
      "td" -> {
        currentParticipantBuilder?.let { participantBuilder ->
          push(ParticipantContentParser(participantBuilder))
        }
      }
    }
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "tr" -> {
        // Attempt to build the participant, and if valid, add to the list builder
        currentParticipantBuilder?.build()?.let { participant ->
          participantListBuilder.add(participant)
        }
        currentParticipantBuilder = null
      }
      "table" -> pop()
    }
  }
}

private class ParticipantContentParser(
  private val participantBuilder: ParticipantBuilder,
) : SoupParser() {

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    when (name) {
      "a" -> {
        // Check href meta for the 'Type' of participant content to expect
        val href = attributes["href"] ?: return
        when {
          PlayerNameRegex.matches(href) -> {
            PlayerNameRegex.find(href)?.let { match ->
              match.groupValues.getOrNull(1)?.let { playerId ->
                participantBuilder.id = playerId
                push(PlayerNameParser(participantBuilder))
              } ?: bark { "Failed to find playerId in (${match.groupValues})" }
            } ?: bark { "Failed to find playerId in ($href)" }
          }
          ArchetypeRegex.matches(href) -> {
            ArchetypeRegex.find(href)?.let { match ->

              match.groupValues.getOrNull(1)?.let { archetypeId ->
                participantBuilder.archetypeId = archetypeId
              } ?: bark { "Failed to find archetype id in (${match.groupValues})" }

              match.groupValues.getOrNull(3)?.let { variant ->
                participantBuilder.archetypeVariant = variant
              } ?: bark { "Failed to find archetype variant in (${match.groupValues})" }

              push(ArchetypeParser(participantBuilder))
            } ?: bark { "Failed to find archetype id in ($href)" }
          }
          DeckListRegex.matches(href) -> {
            DeckListRegex.find(href)?.let { match ->
              match.groupValues.getOrNull(1)?.let { deckListId ->
                participantBuilder.deckListId = deckListId
              } ?: bark { "Failed to find deck list id in (${match.groupValues})" }
            } ?: bark { "Failed to find deck list id in ($href)" }
          }
        }
      }
      "img" -> {
        val countryCode = attributes["alt"] ?: return
        participantBuilder.countryCode = countryCode
      }
    }
  }

  override fun onText(text: String) {
    // At this layer the only "onText" we should see is for their place # in the tournament
    val place = text.toIntOrNull()
    if (place != null) {
      participantBuilder.place = place
    }
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "td" -> pop()
    }
  }

  companion object {
    private val PlayerNameRegex = "/players/(\\d+)".toRegex()
    private val ArchetypeRegex = "/decks/(\\d+)(\\?variant=(\\d+))?".toRegex()
    private val DeckListRegex = "/decks/list/(\\d+)$".toRegex()
  }
}

private class PlayerNameParser(
  private val participantBuilder: ParticipantBuilder,
) : SoupParser() {
  override fun onText(text: String) {
    participantBuilder.name = text
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "a" -> pop()
    }
  }
}

private class ArchetypeParser(
  private val participantBuilder: ParticipantBuilder,
) : SoupParser() {
  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    when (name) {
      "span" -> {
        val archetypeName = attributes["data-tooltip"] ?: return
        participantBuilder.archetypeName = archetypeName
      }
      "img" -> {
        val symbolUrl = attributes["src"] ?: return
        participantBuilder.archetypeSymbols.add(symbolUrl)
      }
    }
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "a" -> pop()
    }
  }
}
