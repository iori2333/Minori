package me.iori.minori.utils.tables

import org.ktorm.schema.*

object Messages : Table<Nothing>("messages") {
  val groupId = long("groupId")
  val senderId = long("senderId")
  val content = text("content")
  val time = int("time")
}
