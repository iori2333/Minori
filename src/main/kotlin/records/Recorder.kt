package me.iori.minori.records

import me.iori.minori.data.MessageCache
import me.iori.minori.data.MessageSQL
import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.GroupMessageEvent

object Recorder {
  lateinit var listener: Listener<GroupMessageEvent>

  fun listen() {
    listener = GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
      val msg = RecordMessage(it.message.serializeToMiraiCode(), it.time)
      if (msg.content.length <= 8) {
        addCache(it.group.id, it.sender.id, msg)
      }
      MessageSQL.insertMessage(it.group.id, it.sender.id, msg)
    }
  }

  fun dispose() {
    MessageSQL.dispose()
    listener.complete()
    consoleLogger.info("Saving cache to disk")
    consoleLogger.info("Cache size: ${MessageCache.cache.entries.sumOf { it.value.size }} entries")
  }

  private fun addCache(group: Long, sender: Long, message: RecordMessage) {
    val key = "$group-$sender"
    if (MessageCache.cache[key] == null) {
      MessageCache.cache[key] = mutableListOf()
    }
    MessageCache.cache[key]!!.add(message)
    if (MessageCache.cache[key]!!.size >= 1000) {
      MessageCache.cache[key]!!.removeFirst()
    }
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
