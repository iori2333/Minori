package me.iori.minori.utils

import me.iori.minori.data.MessageCache
import me.iori.minori.data.MessageSQL
import me.iori.minori.data.RecordMessage

import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.GroupMessageEvent
import kotlin.math.min

object Recorder {
  private lateinit var listener: Listener<GroupMessageEvent>
  private const val MAX_RECALL = 20
  private const val MAX_TOKEN_SIZE = 10
  private const val MAX_CACHE_SIZE = 1000

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

    if (MessageCache.tokens[key] == null) {
      MessageCache.tokens[key] = mutableListOf()
    }

    MessageCache.cache[key]!!.add(message)
    if (MessageCache.cache[key]!!.size >= MAX_CACHE_SIZE) {
      MessageCache.cache[key]!!.removeFirst()
    }
    if (message.content.length <= MAX_TOKEN_SIZE) {
      MessageCache.tokens[key]!!.add(message.content)
      if (MessageCache.tokens[key]!!.size >= MAX_CACHE_SIZE) {
        MessageCache.tokens[key]!!.removeFirst()
      }
    }
  }

  fun randomMessage(group: Long): String {
    return MessageCache.tokens[group.toString()]?.random() ?: ""
  }

  fun recentMessage(group: Long, k: Int): List<RecordMessage> {
    return MessageCache.cache[group.toString()]
      ?.takeLast(min(k, MAX_RECALL)) ?: listOf()
  }

  fun memberMessage(group: Long, sender: Long, k: Int): List<RecordMessage> {
    return MessageCache.cache[group.toString()]
      ?.filter { it.sender == sender }
      ?.take(min(k, MAX_RECALL)) ?: listOf()
  }
}
