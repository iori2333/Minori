package me.iori.minori.commands.simple

import me.iori.minori.data.LanguageData
import me.iori.minori.interfaces.SourceCommand

object LearnCommand : SourceCommand(
  "学什么",
  "别学了别学了",
  entries = LanguageData.courses,
  formats = LanguageData.courseFormats,
)
