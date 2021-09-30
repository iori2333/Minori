package me.iori.minori.utils

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
}