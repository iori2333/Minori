package me.iori.minori.utils

import me.iori.minori.interfaces.RecordMessage
import java.sql.*

object MessageSQL {
  private const val URL = "jdbc:sqlite:data/Minori/minori.sqlite"

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

  private const val SELECTOR = """
    SELECT * FROM messages
    WHERE groupId=? 
    AND senderId=? 
    AND content LIKE ?
    ORDER BY time DESC;
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

  fun select(group: Long, sender: Long, message: String): List<RecordMessage> {
    val stmt = conn.prepareStatement(SELECTOR)
    stmt.setLong(1, group)
    stmt.setLong(2, sender)
    stmt.setString(3, "%${message}%")
    val res = stmt.executeQuery()

    val messages = mutableListOf<RecordMessage>()
    while (res.next() && messages.size < 20) {
      val rec = RecordMessage(
        group = res.getLong(1),
        sender = res.getLong(2),
        content = res.getString(3),
        time = res.getInt(4)
      )
      messages.add(rec)
    }
    return messages
  }
}
