package app.deckbox.tournament.xml

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

/**
 * Reading xml events until one matches initial configuration conditions,
 * upon doing so can push an additional configuration that again is waiting until
 * certain conditions are met, building some model, or pushing additional configurations
 *
 * Skraper {
 *   val tournaments = mutableListOf<Tournament>()
 *   on<StartElementEvent> { event ->
 *     if (event matches ...) {
 *
 *        on<StartElementEvent> {
 *
 *        }
 *
 *     }
 *   }
 * }
 *
 */
object Skraper {

  fun analyze(soupParser: SoupParser, htmlText: String) {
    val handler = NestingSoupHandler(soupParser)

    val parser = KsoupHtmlParser(
      handler = handler,
    )

    parser.reset()
    parser.write(htmlText)
    parser.end()
  }
}
