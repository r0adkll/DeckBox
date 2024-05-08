package app.deckbox.tournament.xml

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

class NestingSoupHandler(
  private val initialHandler: SoupParser,
) : KsoupHtmlHandler, NestingHandler {

  private val handlerStack = ArrayDeque<SoupParser>()

  override fun push(handler: SoupParser) {
    handler.nestingHandler = this
    handlerStack.addFirst(handler)
  }

  override fun pop() {
    handlerStack.removeFirst().apply {
      nestingHandler = null
    }
  }

  override fun onParserInit(ksoupHtmlParser: KsoupHtmlParser) {
    clear()
    push(initialHandler)
  }

  private fun clear() {
    var handler = handlerStack.removeFirstOrNull()
    while (handler != null) {
      handler.nestingHandler = null
      handler = handlerStack.removeFirstOrNull()
    }
  }

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    handlerStack.firstOrNull()?.onOpenTag(name, attributes, isImplied)
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    handlerStack.firstOrNull()?.onCloseTag(name, isImplied)
  }

  override fun onText(text: String) {
    handlerStack.firstOrNull()?.onText(text)
  }

  override fun onAttribute(name: String, value: String, quote: String?) {
    handlerStack.firstOrNull()?.onAttribute(name, value, quote)
  }

  override fun onEnd() {
  }
}

interface NestingHandler {
  fun push(handler: SoupParser)
  fun pop()
}

abstract class SoupParser : KsoupHtmlHandler, NestingHandler {

  internal var nestingHandler: NestingHandler? = null

  override fun push(handler: SoupParser) {
    nestingHandler?.push(handler)
  }

  override fun pop() {
    nestingHandler?.pop()
  }
}
