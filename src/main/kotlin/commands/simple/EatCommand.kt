package me.iori.minori.commands.simple

import me.iori.minori.data.LanguageData
import me.iori.minori.interfaces.SourceCommand

object EatCommand : SourceCommand(
  "吃什么",
  "随机选择今天的饭",
  entries = LanguageData.foods,
  formats = LanguageData.foodFormats,
)
