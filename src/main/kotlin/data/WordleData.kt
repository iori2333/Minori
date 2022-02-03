package me.iori.minori.data

import net.mamoe.mirai.console.data.ReadOnlyPluginData
import net.mamoe.mirai.console.data.value

object WordleData : ReadOnlyPluginData("WordleData") {
  val words: Map<String, List<String>> by value()
}
