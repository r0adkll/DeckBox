package app.deckbox.db

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver

internal object DestructiveMigrationSchema {

  fun perform(
    driver: SqlDriver,
  ) {
    val tables = driver.executeQuery(
      identifier = null,
      sql = "SELECT name FROM sqlite_master WHERE type='table';",
      parameters = 0,
      mapper = { cursor ->
        QueryResult.Value(
          buildList {
            while (cursor.next().value) {
              val name = cursor.getString(0)!!
              if (name != "sqlite_sequence" && name != "android_metadata") {
                add(name)
              }
            }
          },
        )
      },
    ).value

    for (table in tables) {
      driver.execute(identifier = null, sql = "DROP TABLE $table", parameters = 0)
    }
  }
}
