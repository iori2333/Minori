package me.iori.minori.data

import net.mamoe.mirai.console.data.ReadOnlyPluginData
import net.mamoe.mirai.console.data.value

object ResponsesData : ReadOnlyPluginData("ResponsesData") {
  val responses: Map<String, List<String>> by value()
}
