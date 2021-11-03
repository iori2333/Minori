package me.iori.minori.utils

import me.iori.minori.interfaces.RecordMessage
import me.iori.minori.utils.tables.Messages
import org.ktorm.database.Database
import org.ktorm.database.SqlDialect
import org.ktorm.dsl.*
import org.ktorm.support.sqlite.SQLiteDialect

object Database {
  private const val MAX_QUERY = 20

  private val db = Database.connect(
    url = "jdbc:sqlite:data/Minori/minori.sqlite",
    driver = "org.sqlite.JDBC",
    dialect = SQLiteDialect(),
  )

  fun insertMessage(message: RecordMessage) {
    db.insert(Messages) {
      set(Messages.groupId, message.group)
      set(Messages.senderId, message.sender)
      set(Messages.content, message.content)
      set(Messages.time, message.time)
    }
  }

  fun selectMessage(group: Long, sender: Long, message: String): Pair<Int, List<RecordMessage>> {
    val ret = db.from(Messages)
      .select(Messages.content, Messages.time)
      .whereWithConditions {
        it += Messages.groupId eq group
        if (sender != 0L) {
          it += Messages.senderId eq sender
        }
        it += Messages.content like "%$message%"
      }
      .orderBy(Messages.time.desc())
      .limit(MAX_QUERY)
      .map {
        RecordMessage(
          sender = sender,
          group = group,
          content = it.getString(1) ?: "",
          time = it.getInt(2),
        )
      }

    val count = db.from(Messages)
      .select(count())
      .whereWithConditions {
        it += Messages.groupId eq group
        it += Messages.senderId eq sender
        it += Messages.content like "%$message%"
      }
      .map { it.getInt(1) }
      .first()
    return Pair(count, ret)
  }
}
