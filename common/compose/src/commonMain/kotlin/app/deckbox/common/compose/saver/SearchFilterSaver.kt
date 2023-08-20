package app.deckbox.common.compose.saver

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import app.deckbox.core.model.RangeValue
import app.deckbox.core.model.SearchField
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.SuperType
import app.deckbox.core.model.Type
import app.deckbox.core.model.saverValue

object SearchFilterSaver : Saver<SearchFilter, Any> {
  override fun SaverScope.save(value: SearchFilter): Any = listOf(
    value.field,
    value.types,
    value.superTypes,
    value.subTypes,
    value.expansions,
    value.rarity,
    value.retreatCost.saverValue(),
    value.attackCost.saverValue(),
    value.attackDamage.saverValue(),
    value.hp.saverValue(),
    value.evolvesFrom,
    value.evolvesTo,
    value.weaknesses,
    value.resistances,
  )

  @Suppress("UNCHECKED_CAST")
  override fun restore(value: Any): SearchFilter? {
    val listValues = value as? List<*>
    return listValues?.let { values ->
      SearchFilter(
        field = values[0] as SearchField,
        types = values[1] as Set<Type>,
        superTypes = values[2] as Set<SuperType>,
        subTypes = values[3] as Set<String>,
        expansions = values[4] as Set<String>,
        rarity = values[5] as Set<String>,
        retreatCost = RangeValue.fromSaverValue(values[6]),
        attackCost = RangeValue.fromSaverValue(values[7]),
        attackDamage = RangeValue.fromSaverValue(values[8]),
        hp = RangeValue.fromSaverValue(values[9]),
        evolvesFrom = values[10] as? String,
        evolvesTo = values[11] as? String,
        weaknesses = values[12] as Set<Type>,
        resistances = values[13] as Set<Type>,
      )
    }
  }
}
