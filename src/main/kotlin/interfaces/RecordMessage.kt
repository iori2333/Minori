package me.iori.minori.interfaces

import kotlinx.serialization.Serializable

@Serializable
data class RecordMessage(
  val sender: Long = 0L,
  val group: Long = 0L,
  val ids: IntArray = intArrayOf(),
  val internalIds: IntArray = intArrayOf(),
  val time: Int = 0,
  val content: String = "",
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as RecordMessage

    if (sender != other.sender) return false
    if (group != other.group) return false
    if (!ids.contentEquals(other.ids)) return false
    if (!internalIds.contentEquals(other.internalIds)) return false
    if (time != other.time) return false
    if (content != other.content) return false

    return true
  }

  override fun hashCode(): Int {
    var result = sender.hashCode()
    result = 31 * result + group.hashCode()
    result = 31 * result + ids.contentHashCode()
    result = 31 * result + internalIds.contentHashCode()
    result = 31 * result + time
    result = 31 * result + content.hashCode()
    return result
  }
}
