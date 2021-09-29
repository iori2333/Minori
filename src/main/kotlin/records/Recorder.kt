package me.iori.minori.records

import me.iori.minori.data.MessageCache
import me.iori.minori.data.MessageSQL
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent

object Recorder {
  fun listen() {
    GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
      val msg = RecordMessage(it.message.serializeToMiraiCode(), it.time)
      if (msg.content.length <= 8) {
        addMessage(it.group.id, it.sender.id, msg)
      }
    }
  }

  private fun addMessage(group: Long, sender: Long, message: RecordMessage) {
    val key = "$group-$sender"
    if (MessageCache.cache[key] == null) {
      MessageCache.cache[key] = mutableListOf()
    }
    MessageCache.cache[key]!!.add(message)
    if (MessageCache.cache[key]!!.size >= 1000) {
      MessageCache.cache[key]!!.removeFirst()
    }

    MessageSQL.insertMessage(group, sender, message)
  }

  fun randomMessage(group: Long): String {
    return MessageCache.cache.entries
      .filter { it.key.startsWith(group.toString()) }
      .random().value.random().content
  }

  fun memberMessage(group: Long, user: Long): List<RecordMessage> {
    return MessageCache.cache["$group-$user"] ?: listOf()
  }
}
