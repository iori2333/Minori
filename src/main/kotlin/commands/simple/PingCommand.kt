package me.iori.minori.commands.simple

import me.iori.minori.data.LanguageData

object PingCommand : SourceCommandBase(
  "ping",
  "ping & pong!",
  entries = LanguageData.responses,
)
