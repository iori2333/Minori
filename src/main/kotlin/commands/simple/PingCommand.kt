package me.iori.minori.commands.simple

import me.iori.minori.data.LanguageData
import me.iori.minori.interfaces.SourceCommand

object PingCommand : SourceCommand(
  "ping",
  "ping & pong!",
  entries = LanguageData.responses,
)
