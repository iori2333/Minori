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
    listener = GlobalEventChannel.subscribeAlways {
      val msg = RecordMessage(
        it.sender.id,
        it.group.id,
        it.source.ids,
        it.source.internalIds,
        it.source.time,
        it.message.serializeToMiraiCode()
      )

      addCache(msg)
      MessageSQL.insertMessage(it.group.id, it.sender.id, msg)
    }
  }

  fun dispose() {
    MessageSQL.dispose()
    listener.complete()
    consoleLogger.info("Saving cache to disk")
    consoleLogger.info("Cache size: ${MessageCache.cache.entries.sumOf { it.value.size }} entries")
  }

  private fun addCache(message: RecordMessage) {
    val key = message.group.toString()
    if (MessageCache.cache[key] == null) {
      MessageCache.cache[key] = mutableListOf()
    }
    MessageCache.cache[key]!!.add(message)
    if (MessageCache.cache[key]!!.size >= 1000) {
      MessageCache.cache[key]!!.removeFirst()
    }
  }

  fun randomMessage(group: Long): String {
    return MessageCache.cache[group.toString()]
      ?.filter { it -> it.content.length < 8 }
      ?.randomOrNull()?.content ?: ""
  }

  fun recentMessage(group: Long, k: Int): List<RecordMessage> {
    return MessageCache.cache[group.toString()]
      ?.takeLast(k) ?: listOf()
  }

  fun memberMessage(group: Long, user: Long, k: Int): List<RecordMessage> {
    return MessageCache.cache[group.toString()]
      ?.filter { it.sender == user }
      ?.take(k) ?: listOf()
  }
}
