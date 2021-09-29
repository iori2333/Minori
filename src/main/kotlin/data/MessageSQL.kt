package me.iori.minori.data

import me.iori.minori.records.RecordMessage
import java.sql.*

object MessageSQL {
  private const val URL = "jdbc:sqlite:data/Minori/minori.sqlite"
  private const val USER = "root"
  private const val PASSWORD = "root"

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
    VALUES (?, ?, ?, ?)
  """

  private val conn: Connection

  init {
    Class.forName("org.sqlite.JDBC")
    conn = DriverManager.getConnection(URL, USER, PASSWORD)
    val stmt = conn.createStatement()
    stmt.execute(CREATE_TABLE)
  }

  fun insertMessage(group: Long, sender: Long, message: RecordMessage) {
    val stmt = conn.prepareStatement(INSERT_MESSAGE)
    stmt.setLong(1, group)
    stmt.setLong(2, sender)
    stmt.setString(3, message.content)
    stmt.setInt(4, message.time)

    stmt.execute()
  }
}
