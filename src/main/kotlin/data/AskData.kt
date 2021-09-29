package me.iori.minori.data

import net.mamoe.mirai.console.data.ReadOnlyPluginData
import net.mamoe.mirai.console.data.value

object AskData : ReadOnlyPluginData("AskData") {
  val doings: List<String> by value()
  val places: Map<String, List<String>> by value()
  val excludedR10s: List<String> by value()
  val excludedPrefixes: List<String> by value()
  val excludedChoiceWords: List<String> by value()
}
