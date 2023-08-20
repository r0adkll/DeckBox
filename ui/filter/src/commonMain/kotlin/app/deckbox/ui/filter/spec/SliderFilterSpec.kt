package app.deckbox.ui.filter.spec

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.GreaterThan
import app.deckbox.common.compose.icons.rounded.GreaterThanEqual
import app.deckbox.common.compose.icons.rounded.LessThan
import app.deckbox.common.compose.icons.rounded.LessThanEqual
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.RangeValue
import app.deckbox.core.model.SearchFilter
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiState
import cafe.adriel.lyricist.LocalStrings
import kotlin.math.roundToInt

data class SliderFilterAction(
  private val value: RangeValue,
  private val applicator: SearchFilter.(RangeValue) -> SearchFilter,
) : FilterAction {
  override fun applyToFilter(
    expansions: List<Expansion>,
    filter: SearchFilter,
  ): SearchFilter {
    return filter.applicator(value)
  }
}

abstract class SliderFilterSpec : FilterSpec() {

  abstract val range: ClosedFloatingPointRange<Float>

  abstract fun mapValue(filter: SearchFilter): RangeValue

  abstract fun applyValue(
    filter: SearchFilter,
    value: RangeValue,
  ): SearchFilter

  /**
   * Override this to provide the label content
   */
  @Composable
  open fun RowScope.LabelContent(value: RangeValue) {
    Text(
      text = when (value.modifier) {
        RangeValue.Modifier.LT -> LocalStrings.current.lessThan(value.value)
        RangeValue.Modifier.LTE -> LocalStrings.current.lessThanEqual(value.value)
        RangeValue.Modifier.GT -> LocalStrings.current.greaterThan(value.value)
        RangeValue.Modifier.GTE -> LocalStrings.current.greaterThanEqual(value.value)
        RangeValue.Modifier.NONE -> "${value.value}"
      },
      style = MaterialTheme.typography.labelLarge,
      textAlign = TextAlign.End,
      modifier = Modifier
        .weight(1f)
        .padding(end = 16.dp),
    )
  }

  override fun LazyListScope.buildContent(
    uiState: FilterUiState,
    actionEmitter: (FilterUiEvent) -> Unit,
  ) {
    item {
      val value = mapValue(uiState.filter)
      var sliderValue by remember { mutableStateOf(value.value.toFloat()) }
      var modifierValue by remember { mutableStateOf(value.modifier) }
      Row { LabelContent(RangeValue(sliderValue.roundToInt(), modifierValue)) }
      Slider(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        value = sliderValue,
        valueRange = range,
        onValueChange = { newValue ->
          sliderValue = newValue
        },
        onValueChangeFinished = {
          actionEmitter(
            FilterUiEvent.FilterChange(
              SliderFilterAction(
                value = RangeValue(
                  value = sliderValue.roundToInt(),
                  modifier = modifierValue,
                ),
                applicator = { newValue ->
                  applyValue(uiState.filter, newValue)
                },
              ),
            ),
          )
        },
      )
      Row(Modifier.padding(horizontal = 12.dp)) {
        ModifierIconButton(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
          rangeModifier = RangeValue.Modifier.LT,
          getter = { modifierValue },
          setter = { modifierValue = it },
        )
        ModifierIconButton(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
          rangeModifier = RangeValue.Modifier.LTE,
          getter = { modifierValue },
          setter = { modifierValue = it },
        )
        ModifierIconButton(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
          rangeModifier = RangeValue.Modifier.GT,
          getter = { modifierValue },
          setter = { modifierValue = it },
        )
        ModifierIconButton(
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
          rangeModifier = RangeValue.Modifier.GTE,
          getter = { modifierValue },
          setter = { modifierValue = it },
        )
      }
    }
  }
}

@Composable
private fun ModifierIconButton(
  rangeModifier: RangeValue.Modifier,
  modifier: Modifier = Modifier,
  getter: () -> RangeValue.Modifier?,
  setter: (RangeValue.Modifier) -> Unit,
) {
  val isSelected = getter() == rangeModifier
  IconButton(
    modifier = modifier,
    onClick = {
      if (isSelected) {
        setter(RangeValue.Modifier.NONE)
      } else {
        setter(rangeModifier)
      }
    },
  ) {
    Icon(
      when (rangeModifier) {
        RangeValue.Modifier.LT -> Icons.Rounded.LessThan
        RangeValue.Modifier.LTE -> Icons.Rounded.LessThanEqual
        RangeValue.Modifier.GT -> Icons.Rounded.GreaterThan
        RangeValue.Modifier.GTE -> Icons.Rounded.GreaterThanEqual
        RangeValue.Modifier.NONE -> throw IllegalStateException("Can't use that modifier for a button")
      },
      contentDescription = null,
      modifier = Modifier.alpha(if (isSelected) 1f else 0.26f),
    )
  }
}

object HpFilterSpec : SliderFilterSpec() {
  override val title: String = "HP"
  override val range: ClosedFloatingPointRange<Float> = 0f..400f

  override fun mapValue(filter: SearchFilter): RangeValue = filter.hp ?: RangeValue(0)

  override fun applyValue(filter: SearchFilter, value: RangeValue): SearchFilter {
    return filter.copy(hp = value)
  }
}

object AttackCostFilterSpec : SliderFilterSpec() {
  override val title: String = "Attack Cost"
  override val range: ClosedFloatingPointRange<Float> = 0f..6f

  override fun mapValue(filter: SearchFilter): RangeValue = filter.attackCost
    ?: RangeValue(0)

  override fun applyValue(filter: SearchFilter, value: RangeValue): SearchFilter {
    return filter.copy(attackCost = value)
  }
}

object AttackDamageFilterSpec : SliderFilterSpec() {
  override val title: String = "Attack Damage"
  override val range: ClosedFloatingPointRange<Float> = 0f..300f

  override fun mapValue(filter: SearchFilter): RangeValue = filter.attackDamage
    ?: RangeValue(0)

  override fun applyValue(filter: SearchFilter, value: RangeValue): SearchFilter {
    return filter.copy(attackDamage = value)
  }
}

object RetreatCostFilterSpec : SliderFilterSpec() {
  override val title: String = "Retreat Cost"
  override val range: ClosedFloatingPointRange<Float> = 0f..4f

  override fun mapValue(filter: SearchFilter): RangeValue = filter.retreatCost
    ?: RangeValue(0)

  override fun applyValue(filter: SearchFilter, value: RangeValue): SearchFilter {
    return filter.copy(retreatCost = value)
  }
}
