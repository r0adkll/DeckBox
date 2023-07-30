package app.deckbox.db

import app.cash.sqldelight.ColumnAdapter
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

object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
  override fun decode(databaseValue: String): LocalDate = LocalDate.parse(databaseValue)
  override fun encode(value: LocalDate): String = value.toString()
}

object LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
  override fun decode(databaseValue: String): LocalDateTime = LocalDateTime.parse(databaseValue)
  override fun encode(value: LocalDateTime): String = value.toString()
}
