package app.deckbox.core.logging

typealias Extras = Map<String, String>

class Heartwood private constructor() {

  interface Bark {
    fun log(priority: LogPriority, tag: String?, extras: Extras?, message: String)
  }

  companion object : Bark {
    private val barks = mutableListOf<Bark>()

    fun grow(bark: Bark) {
      barks.add(bark)
    }

    override fun log(priority: LogPriority, tag: String?, extras: Extras?, message: String) {
      barks.forEach { it.log(priority, tag, extras, message) }
    }
  }
}

enum class LogPriority(val priority: Int) {
  VERBOSE(2),
  DEBUG(3),
  INFO(4),
  WARN(5),
  ERROR(6),
}

/**
 * Bark out a message into the world for all to hear.
 */
inline fun Any.bark(
  priority: LogPriority = LogPriority.DEBUG,
  /**
   * If provided, the log will use this tag instead of the simple class name of `this` at the call
   * site.
   */
  tag: String? = null,
  extras: Extras? = null,
  message: () -> String,
) {
  val tagOrCaller = tag ?: outerClassSimpleNameInternalOnlyDoNotUseKThxBye()
  Heartwood.log(priority, tagOrCaller, extras, message())
}

/**
 * An overload for logging that does not capture the calling code as tag. This should only
 * be used in standalone functions where there is no `this`.
 * @see logcat above
 */
inline fun bark(
  tag: String,
  priority: LogPriority = LogPriority.DEBUG,
  extras: Extras? = null,
  message: () -> String,
) {
  Heartwood.log(priority, tag, extras, message())
}

@PublishedApi
internal fun Any.outerClassSimpleNameInternalOnlyDoNotUseKThxBye(): String? {
  val fullClassName = this::class.qualifiedName ?: return null
  val outerClassName = fullClassName.substringBefore('$')
  val simplerOuterClassName = outerClassName.substringAfterLast('.')
  return if (simplerOuterClassName.isEmpty()) {
    fullClassName
  } else {
    simplerOuterClassName.removeSuffix("Kt")
  }
}
