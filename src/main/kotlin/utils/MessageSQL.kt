package me.iori.minori.utils

import me.iori.minori.interfaces.RecordMessage
import java.sql.*

@Deprecated(
  message = "Pure SQL version of database is now deprecated",
  replaceWith = ReplaceWith("MessageDb", "me.iori.minori.utils.MinoriDb")
)
object MessageSQL {
  private const val URL = "jdbc:sqlite:data/Minori/minori.sqlite"

  private const val MAX_QUERY = 20

  private const val CREATE_TABLE = """
    CREATE TABLE IF NOT EXISTS messages
    (
      groupId  INTEGER,
      senderId INTEGER,
      content  TEXT,
      time     INTEGER
    );
  """
  private const val INSERT_MESSAGE = """
    INSERT INTO messages
    VALUES (?, ?, ?, ?);
  """

  private const val SELECT_MSG = """
    SELECT * FROM messages
    WHERE groupId=? 
    AND senderId=? 
    AND content LIKE ?
    ORDER BY time DESC
    LIMIT $MAX_QUERY;
  """

  private const val SELECT_COUNT = """
    SELECT COUNT(*) FROM messages
    WHERE groupId=? 
    AND senderId=? 
    AND content LIKE ?
  """

  private val conn: Connection

  init {
    Class.forName("org.sqlite.JDBC")
    conn = DriverManager.getConnection(URL)
    val stmt = conn.createStatement()
    stmt.execute(CREATE_TABLE)
  }

  fun dispose() {
    conn.close()
  }

  fun insertMessage(message: RecordMessage) {
    val stmt = conn.prepareStatement(INSERT_MESSAGE)
    stmt.setLong(1, message.group)
    stmt.setLong(2, message.sender)
    stmt.setString(3, message.content)
    stmt.setInt(4, message.time)

    stmt.execute()
  }

  fun selectMessage(group: Long, sender: Long, message: String): Pair<Int, List<RecordMessage>> {
    val stmt1 = conn.prepareStatement(SELECT_MSG)
    val stmt2 = conn.prepareStatement(SELECT_COUNT)
    stmt1.setLong(1, group)
    stmt1.setLong(2, sender)
    stmt1.setString(3, "%${message}%")

    stmt2.setLong(1, group)
    stmt2.setLong(2, sender)
    stmt2.setString(3, "%${message}%")

    val res = stmt1.executeQuery()
    val messages = mutableListOf<RecordMessage>()
    while (res.next()) {
      val rec = RecordMessage(
        group = res.getLong(1),
        sender = res.getLong(2),
        content = res.getString(3),
        time = res.getInt(4)
      )
      messages.add(rec)
    }
    val count = stmt2.executeQuery().getInt(1)
    return Pair(count, messages)
  }
}
