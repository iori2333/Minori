package me.iori.minori.data

import net.mamoe.mirai.console.data.ReadOnlyPluginData
import net.mamoe.mirai.console.data.value

object LanguageData : ReadOnlyPluginData("LanguageData") {
  // for AskCommand
  val doings: List<String> by value()
  val places: Map<String, List<String>> by value()
  val excludedR10s: List<String> by value()
  val excludedPrefixes: List<String> by value()
  val excludedChoiceWords: List<String> by value()

  // for LuckCommand
  val events: List<String> by value()
  val ranks: List<String> by value()

  // for EatCommand
  val foods: List<String> by value()
  val foodFormats: List<String> by value()

  // for LearnCommand
  val courses: List<String> by value()
  val courseFormats: List<String> by value()

  // for PingCommand
  val responses: List<String> by value()
}
