package me.iori.minori.processors.inlines

import me.iori.minori.interfaces.InlineCommand

object ShuffleInline : InlineCommand(
  "shuffle",
  Regex("shuffle (.*)")
) {
  override fun parse(text: String): String {
    val match = reg.matchEntire(text) as MatchResult
    val words = match.groupValues[1].split(" ")
    return if (words.size > 1) {
      words.shuffled().joinToString(" ")
    } else {
      words[0].toList().shuffled().joinToString("")
    }
  }
}
