package me.iori.minori.commands.simple

import me.iori.minori.data.LanguageData

object EatCommand : SourceCommandBase(
  "吃什么",
  "随机选择今天的饭",
  entries = LanguageData.foods,
  formats = LanguageData.foodFormats,
)
