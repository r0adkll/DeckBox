package app.deckbox.tournament.xml

import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.XmlEvent
import nl.adaptivity.xmlutil.core.KtXmlReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.StringReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.use
import nl.adaptivity.xmlutil.toEvent

class Skraper {

  @OptIn(ExperimentalXmlUtilApi::class)
  fun analyze(htmlText: String) {
    KtXmlReader(StringReader(htmlText)).use { reader ->
      while (reader.hasNext()) {
        val event = reader.next()
        when (event) {
          EventType.START_DOCUMENT -> TODO()
          EventType.START_ELEMENT -> {
            val startElementEvent = reader.toEvent() as XmlEvent.StartElementEvent

          }
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
  }
}
