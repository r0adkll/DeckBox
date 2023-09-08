package app.deckbox.features.cards.public.model

import app.deckbox.core.model.RangeValue
import app.deckbox.core.model.SearchField
import app.deckbox.core.model.SearchFilter

const val MAX_PAGE_SIZE = 250

data class CardQuery(
  val query: String? = null,
  val orderBy: String? = null,
  val page: Int = 1,
  val pageSize: Int = MAX_PAGE_SIZE,
  val filter: SearchFilter? = null,
) {

  /**
   * Get the unique key for this query that we can use to do some caching
   * for better user-ex
   */
  val key: String
    get() = buildString {
      query?.let { append(it) }
      orderBy?.let { append(it) }
      append(generateQueryParam())
    }.ifBlank { "DEFAULT" }

  fun asQueryOptions(): Map<String, String> = buildMap {
    put("page", "$page")
    put("pageSize", "$pageSize")
    orderBy?.let { put("orderBy", it) }
    if (query != null || filter != null) {
      put("q", generateQueryParam())
    }
  }

  private fun generateQueryParam(): String {
    return buildString {
      if (!query.isNullOrBlank()) {
        when (filter?.field) {
          SearchField.TEXT -> appendValue("flavorText", query)
          SearchField.ABILITY_NAME -> appendValue("abilities.name", query)
          SearchField.ABILITY_TEXT -> appendValue("abilities.text", query)
          SearchField.ATTACK_NAME -> appendValue("attacks.name", query)
          SearchField.ATTACK_TEXT -> appendValue("attacks.text", query)
          else -> appendValue("name", query)
        }
      }

      if (filter != null) {
        appendOrList("subtypes", filter.subTypes)
        appendOrList("types", filter.types.map { it.name.lowercase() })
        appendOrList("supertype", filter.superTypes.map { it.text.lowercase() })
        appendOrList("rarity", filter.rarity)
        appendOrList("set.id", filter.expansions)
        appendOrList("weaknesses.type", filter.weaknesses.map { it.name.lowercase() })
        appendOrList("resistances.type", filter.resistances.map { it.name.lowercase() })

        filter.hp?.let { appendRange("hp", it) }
        filter.retreatCost?.let { appendRange("convertedRetreatCost", it) }
        filter.attackCost?.let { appendRange("attack.convertedEnergyCost", it) }
        filter.attackDamage?.let { appendRange("attack.damage", it) }

        filter.evolvesFrom?.let { appendValue("evolvesFrom", it) }
        filter.evolvesTo?.let { appendValue("evolvesTo", it) }
      }
    }.trim()
  }
}

fun buildQuery(
  page: Int = 1,
  pageSize: Int = MAX_PAGE_SIZE,
  builder: StringBuilder.() -> Unit,
): Map<String, String> {
  return buildMap {
    put("page", page.toString())
    put("pageSize", pageSize.toString())
    put("q", buildString(builder))
  }
}

fun StringBuilder.appendValue(
  field: String,
  value: String,
) {
  val quotedValue = if (value.contains(SPACE)) {
    "\"$value\""
  } else {
    value
  }
  append("$field:$quotedValue")
  append(SPACE)
}

fun StringBuilder.appendOrList(
  field: String,
  options: Collection<String>,
) {
  if (options.isEmpty()) return
  if (options.size == 1) {
    append("$field:${options.first()}")
  } else {
    append(
      options.joinToString(
        prefix = "(",
        postfix = ")",
        separator = " OR ",
      ) { "$field:$it" },
    )
  }
  append(SPACE)
}

fun StringBuilder.appendRange(
  field: String,
  rangeValue: RangeValue,
) {
  if (rangeValue.isEmpty) return
  val value = when (rangeValue.modifier) {
    RangeValue.Modifier.LT -> append("{* TO ${rangeValue.value}}")
    RangeValue.Modifier.LTE -> append("[* TO ${rangeValue.value}]")
    RangeValue.Modifier.GT -> append("{${rangeValue.value} TO *}")
    RangeValue.Modifier.GTE -> append("[${rangeValue.value} TO *]")
    RangeValue.Modifier.NONE -> append(rangeValue.value)
  }
  append("$field:$value")
  append(SPACE)
}

internal const val SPACE = " "
