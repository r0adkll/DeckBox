package app.deckbox.core.model

data class CollectionCount(
  val cardId: String,

  val normalCount: Int = 0,
  val holofoilCount: Int = 0,
  val reverseHolofoilCount: Int = 0,
  val firstEditionNormalCount: Int = 0,
  val firstEditionHolofoilCount: Int = 0,
) {

  val totalCount: Int
    get() = normalCount +
      holofoilCount +
      reverseHolofoilCount +
      firstEditionNormalCount +
      firstEditionHolofoilCount

  fun forVariant(variant: Card.Variant): Int {
    return when (variant) {
      Card.Variant.Normal -> normalCount
      Card.Variant.Holofoil -> holofoilCount
      Card.Variant.ReverseHolofoil -> reverseHolofoilCount
      Card.Variant.FirstEditionNormal -> firstEditionNormalCount
      Card.Variant.FirstEditionHolofoil -> firstEditionHolofoilCount
    }
  }
}
