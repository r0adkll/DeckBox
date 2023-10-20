package app.deckbox.tournament.xml

import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.XmlReader

interface XmlParser {

  fun onEvent(reader: XmlReader, eventType: EventType)
}
