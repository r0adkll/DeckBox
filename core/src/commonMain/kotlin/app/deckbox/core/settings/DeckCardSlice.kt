package app.deckbox.core.settings

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable
sealed interface DeckCardSlice {

  @Serializable
  sealed interface Header : DeckCardSlice {
    @Serializable data object Export : Header

    @Serializable data object Thumbnail : Header
  }

  @Serializable
  sealed interface Images : DeckCardSlice {
    @Serializable data object Fanned : Images

    @Serializable data object Grid : Images
  }

  @Serializable
  data object Tags : DeckCardSlice

  @Serializable
  data object Description : DeckCardSlice

  @Serializable
  sealed interface Actions : DeckCardSlice {
    @Serializable data object Full : Actions

    @Serializable data object Compact : Actions
  }

  companion object
}

private val deckCardSliceJson by lazy {
  Json {
    serializersModule = SerializersModule {
      polymorphic(DeckCardSlice::class) {
        subclass(DeckCardSlice.Header.Export::class)
        subclass(DeckCardSlice.Header.Thumbnail::class)
        subclass(DeckCardSlice.Images.Fanned::class)
        subclass(DeckCardSlice.Images.Grid::class)
        subclass(DeckCardSlice.Actions.Full::class)
        subclass(DeckCardSlice.Actions.Compact::class)
      }
    }
  }
}

fun List<DeckCardSlice>.asString(): String {
  val serializer = ListSerializer(DeckCardSlice.serializer())
  return deckCardSliceJson.encodeToString(serializer, this)
}

fun DeckCardSlice.Companion.fromString(value: String): List<DeckCardSlice> {
  val serializer = ListSerializer(serializer())
  return deckCardSliceJson.decodeFromString(serializer, value)
}
