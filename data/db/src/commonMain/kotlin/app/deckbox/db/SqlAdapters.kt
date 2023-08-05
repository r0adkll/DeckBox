package app.deckbox.db

import app.cash.sqldelight.ColumnAdapter
import app.deckbox.core.model.Card
import app.deckbox.core.model.Type
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

object StringListAdapter : ColumnAdapter<List<String>, String> {
  override fun decode(databaseValue: String): List<String> {
    return if (databaseValue.isEmpty()) {
      listOf()
    } else {
      databaseValue.split(",")
    }
  }

  override fun encode(value: List<String>) = value.joinToString(separator = ",")
}

object IntListAdapter : ColumnAdapter<List<Int>, String> {
  override fun decode(databaseValue: String): List<Int> {
    return if (databaseValue.isEmpty()) {
      listOf()
    } else {
      databaseValue.split(",").map { it.toInt() }
    }
  }

  override fun encode(value: List<Int>) = value.joinToString(separator = ",")
}

object TypeListAdapter : ColumnAdapter<List<Type>, String> {
  override fun decode(databaseValue: String): List<Type> {
    return databaseValue.split(",")
      .map {
        Type.find(it.trim())
      }
      .filter { it != Type.UNKNOWN }
  }

  override fun encode(value: List<Type>): String {
    return value.joinToString { it.name }
  }
}

object CardEffectListAdapter : ColumnAdapter<List<Card.Effect>, String> {
  override fun decode(databaseValue: String): List<Card.Effect> {
    return databaseValue
      .split(",")
      .mapNotNull {
        val parts = it.split("|")
        if (parts.size == 2) {
          Card.Effect(Type.find(parts[0]), parts[1])
        } else {
          null
        }
      }
  }

  override fun encode(value: List<Card.Effect>): String {
    return value.joinToString {
      "${it.type.name}|${it.value}"
    }
  }
}

object TypeAdapter : ColumnAdapter<Type, String> {
  override fun encode(value: Type): String = value.name
  override fun decode(databaseValue: String): Type = Type.find(databaseValue)
}

object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
  override fun decode(databaseValue: String): LocalDate = LocalDate.parse(databaseValue)
  override fun encode(value: LocalDate): String = value.toString()
}

object LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
  override fun decode(databaseValue: String): LocalDateTime = LocalDateTime.parse(databaseValue)
  override fun encode(value: LocalDateTime): String = value.toString()
}
