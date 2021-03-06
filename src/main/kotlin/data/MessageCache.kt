package me.iori.minori.data

import me.iori.minori.interfaces.RecordMessage
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object MessageCache : AutoSavePluginData("MessageCache") {
  val cache: MutableMap<String, MutableList<RecordMessage>> by value()
  val tokens: MutableMap<String, MutableList<String>> by value()
}
