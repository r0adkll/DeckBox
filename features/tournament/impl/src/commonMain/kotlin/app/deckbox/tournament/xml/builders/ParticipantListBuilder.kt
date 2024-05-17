package app.deckbox.tournament.xml.builders

import app.deckbox.core.logging.bark
import app.deckbox.tournament.api.model.DeckArchetype
import app.deckbox.tournament.api.model.Participant

class ParticipantListBuilder {

  private val participants = mutableListOf<Participant>()

  fun add(participant: Participant) {
    participants += participant
  }

  fun build(): List<Participant> {
    return participants
  }
}

class ParticipantBuilder {

  var id: String? = null
  var name: String? = null
  var place: Int? = null
  var countryCode: String? = null
  var archetypeId: String? = null
  var archetypeName: String? = null
  var archetypeVariant: String? = null
  val archetypeSymbols = mutableListOf<String>()
  var deckListId: String? = null

  fun build(): Participant? {
    if (
      id != null &&
      name != null &&
      place != null &&
      countryCode != null &&
      archetypeId != null &&
      archetypeName != null
    ) {
      return Participant(
        id = id!!,
        name = name!!,
        country = countryCode!!,
        place = place!!,
        archetype = DeckArchetype(
          id = archetypeId!!,
          name = archetypeName!!,
          variant = archetypeVariant,
          symbols = archetypeSymbols,
        ),
        deckListId = deckListId,
      )
    } else {
      bark { "ParticipantBuilder: Missing required fields [$id, $name, $place, $countryCode, $archetypeId, $archetypeName, $deckListId]" }
      return null
    }
  }
}
