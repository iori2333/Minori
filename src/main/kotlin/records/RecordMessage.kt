package me.iori.minori.records

import kotlinx.serialization.Serializable

@Serializable
data class RecordMessage(
  val content: String = "",
  val time: Int = 0
)
