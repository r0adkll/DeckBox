package app.deckbox.tournament.xml

import app.deckbox.tournament.api.model.Tournament
import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.XmlEvent
import nl.adaptivity.xmlutil.XmlReader
import nl.adaptivity.xmlutil.toCName

class TournamentsParser : XmlParser {
  private val tournaments = mutableListOf<Tournament>()

  /**
   * Here we need to scan until a certain section of the HTML xml and then engage look outs for individual
   * tournament elements in the Document.
   */
  override fun onEvent(reader: XmlReader, eventType: EventType) {
    when (eventType) {
      EventType.START_ELEMENT -> {
        val event = eventType.createEvent(reader) as XmlEvent.StartElementEvent
        if (event.localName == "table") {
          val isTournamentsTable = event.attributes
            .find { it.localName == "class" }?.value
            ?.contains(TOURNAMENT_CLASS_ID) == true

          if (isTournamentsTable) {
            // Start listening for tags and such for individual tournaments

          }
        }
      }
      EventType.END_ELEMENT -> {
        val event = eventType.createEvent(reader) as XmlEvent.EndElementEvent
        if (event.localName == "table") {
          // Pop the parser
        }
      }
      else -> Unit // Ignore other events
    }
  }

  companion object {
    private const val TOURNAMENT_CLASS_ID = "completed-tournaments"
  }
}

//language=html
val Example = """
  <table class="data-table striped completed-tournaments">
      // …
      <tr data-date="2023-09-23" data-country="BR" data-name="Regional Curitiba" data-format="standard" data-players="379" data-winner="BR-William Azevedo">
          <td>23 Sep 23</td>
          <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/BR.png" alt="BR" data-tooltip="Brazil"></td>
          <td><a href="/tournaments/385">Regional Curitiba</a></td>
          <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
          <td class="landscape-only">379</td>
          <td class="winner landscape-only">
              <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/BR.png" alt="BR" data-tooltip="Brazil">
              <a class="inline" href="/players/649">William Azevedo</a>
          </td>
      </tr>
  </table>
""".trimIndent()

class TournamentParser : XmlParser {

  override fun onEvent(reader: XmlReader, eventType: EventType) {
    when (eventType) {
      EventType.START_DOCUMENT -> TODO()
      EventType.START_ELEMENT -> TODO()
      EventType.END_ELEMENT -> TODO()
      EventType.COMMENT -> TODO()
      EventType.TEXT -> TODO()
      EventType.CDSECT -> TODO()
      EventType.DOCDECL -> TODO()
      EventType.END_DOCUMENT -> TODO()
      EventType.ENTITY_REF -> TODO()
      EventType.IGNORABLE_WHITESPACE -> TODO()
      EventType.ATTRIBUTE -> TODO()
      EventType.PROCESSING_INSTRUCTION -> TODO()
    }
  }
}
