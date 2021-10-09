package me.iori.minori.processors.inlines

import me.iori.minori.interfaces.InlineCommand

object JoinInline : InlineCommand(
  "join",
  Regex("join (.+)")
) {
  override fun parse(text: String): String {
    val match = reg.matchEntire(text) as MatchResult
    return match.groupValues[1].replace(" ", "")
  }
}