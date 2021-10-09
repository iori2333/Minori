package me.iori.minori.processors.inlines

import me.iori.minori.interfaces.InlineCommand

object ChoiceInline : InlineCommand(
  "choice",
  Regex("choice (.+)"),
) {
  override fun parse(text: String): String {
    val match = reg.matchEntire(text) as MatchResult
    return match.groupValues[1]
      .split(" ").random()
  }
}
