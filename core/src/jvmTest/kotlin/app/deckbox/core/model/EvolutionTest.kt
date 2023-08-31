package app.deckbox.core.model

import kotlin.test.Test
import strikt.api.expectThat
import strikt.assertions.elementAt
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.withFirst

class EvolutionTest {

  @Test
  fun `single card creates a single chain`() {
    // given
    val card = card("0", "Charmander")

    // when
    val result = Evolution.create(listOf(card.stack()))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 1
        get { nodes }
          .withFirst {
            get { cards }
              .hasSize(1)
              .get(0)
              .get { card.name } isEqualTo "Charmander"
          }
      }
  }

  @Test
  fun `in-order evolution creates a chain`() {
    // given
    val basic = card("0", "Charmander").stack()
    val stage1 = card("1", "Charmeleon", evolvesFrom = "Charmander").stack()
    val stage2 = card("2", "Charizard", evolvesFrom = "Charmeleon").stack()

    // when
    val result = Evolution.create(listOf(basic, stage1, stage2))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 3
        with({ nodes }) {
          elementAt(0).hasName("Charmander")
          elementAt(1).hasName("Charmeleon")
          elementAt(2).hasName("Charizard")
        }
      }
  }

  @Test
  fun `out-of-order evolution creates a chain`() {
    // given
    val basic = card("0", "Charmander").stack()
    val stage1 = card("1", "Charmeleon", evolvesFrom = "Charmander").stack()
    val stage2 = card("2", "Charizard", evolvesFrom = "Charmeleon").stack()

    // when
    val result = Evolution.create(listOf(stage2, stage1, basic))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 3
        with({ nodes }) {
          elementAt(0).hasName("Charmander")
          elementAt(1).hasName("Charmeleon")
          elementAt(2).hasName("Charizard")
        }
      }
  }

  @Test
  fun `in-order missing middle evolution forms a single chain`() {
    // given
    val basic = card("0", "Charmander", evolvesTo = listOf("Charmeleon")).stack()
    val stage2 = card("2", "Charizard", evolvesFrom = "Charmeleon").stack()

    // when
    val result = Evolution.create(listOf(basic, stage2))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 2
        with({ nodes }) {
          elementAt(0).hasName("Charmander")
          elementAt(1).hasName("Charizard")
        }
      }
  }

  @Test
  fun `out-of-order missing middle evolution forms a single chain`() {
    // given
    val basic = card("0", "Charmander", evolvesTo = listOf("Charmeleon")).stack()
    val stage2 = card("2", "Charizard", evolvesFrom = "Charmeleon").stack()

    // when
    val result = Evolution.create(listOf(stage2, basic))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 2
        with({ nodes }) {
          elementAt(0).hasName("Charmander")
          elementAt(1).hasName("Charizard")
        }
      }
  }

  @Test
  fun `multiple direct node matches get collected`() {
    // given
    val basic1 = card(id = "sm1-5", name = "Eevee").stack()
    val basic2 = card(id = "sm1-6", name = "Eevee").stack()
    val basic3 = card(id = "sm1-7", name = "Eevee").stack()
    val stage1 = card(id = "sm1-9", name = "Espeon-GX", evolvesFrom = "Eevee").stack()

    // when
    val result = Evolution.create(listOf(basic1, stage1, basic2, basic3))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 2
        with({ nodes }) {
          elementAt(0)
            .hasName("Eevee")
            .with({ cards }) {
              hasSize(3)
              elementAt(0).get { card.id } isEqualTo basic1.card.id
              elementAt(1).get { card.id } isEqualTo basic2.card.id
              elementAt(2).get { card.id } isEqualTo basic3.card.id
            }
        }
      }
  }

  @Test
  fun `multiple sibling node matches get collected`() {
    // given
    val basic = card(id = "sm1-5", name = "Eevee").stack()
    val stage11 = card(id = "sm1-10", name = "Vaporeon-GX", evolvesFrom = "Eevee").stack()
    val stage12 = card(id = "sm1-11", name = "Flareon-GX", evolvesFrom = "Eevee").stack()
    val stage13 = card(id = "sm1-12", name = "Jolteon-GX", evolvesFrom = "Eevee").stack()

    // when
    val result = Evolution.create(listOf(stage11, basic, stage12, stage13))

    // then
    expectThat(result)
      .hasSize(1)
      .withFirst {
        get { size } isEqualTo 2
        with({ nodes }) {
          elementAt(0).hasName("Eevee")
          elementAt(1)
            .hasName("Vaporeon-GX")
            .with({ cards }) {
              hasSize(3)
              elementAt(0).get { card.name } isEqualTo stage11.card.name
              elementAt(1).get { card.name } isEqualTo stage12.card.name
              elementAt(2).get { card.name } isEqualTo stage13.card.name
            }
        }
      }
  }

  @Test
  fun `equality check`() {
    // given
    val basic = card("0", "Charmander", evolvesTo = listOf("Charmeleon")).stack()
    val stage1 = card("1", "Charmeleon", evolvesFrom = "Charmander").stack()
    val stage2 = card("2", "Charizard", evolvesFrom = "Charmeleon").stack()

    // when
    val chain1 = Evolution.create(listOf(basic, stage1, stage2))
    val chain2 = Evolution.create(listOf(stage2, basic, stage1))

    // then
    expectThat(chain1) isEqualTo chain2
  }
}
