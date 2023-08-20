package app.deckbox.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class SearchFilterTest {

  @Test
  fun `attack damage with + returns raw number`() {
    // given
    val attack = Card.Attack(
      cost = null,
      name = "",
      text = null,
      damage = "40+",
      convertedEnergyCost = 0,
    )

    // when
    val result = attack.damageAmount

    // then
    assertEquals(40, result)
  }
}
