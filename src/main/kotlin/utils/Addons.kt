package me.iori.minori.utils

import me.iori.minori.interfaces.RecordMessage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.PlainText

object Addons {
  fun PlainText.replace(from: String, to: String): PlainText {
    return PlainText(content.replace(from, to))
  }

  fun PlainText.replaceAll(pairs: List<Pair<String, String>>): PlainText {
    var text = content
    pairs.forEach { (from, to) ->
      text = text.replace(from, to)
    }
    return PlainText(text)
  }

  fun MessageEvent.toRecord(): RecordMessage {
    return RecordMessage(
      this.sender.id,
      if (this is GroupMessageEvent) this.group.id else 0,
      this.source.ids,
      this.source.internalIds,
      this.source.time,
      this.message.serializeToMiraiCode()
    )
  }
}
