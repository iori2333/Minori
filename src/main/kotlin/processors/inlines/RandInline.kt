package me.iori.minori.processors.inlines

import kotlin.random.Random

object RandInline : InlineCommand(
  "rand",
  Regex("rand (\\d+) (\\d+)")
) {
  override fun parse(text: String): String {
    val match = reg.matchEntire(text) as MatchResult
    val (left, right) = match.groupValues.subList(1, 3).map { it.toInt() }
    return Random.nextInt(left, right).toString()
  }
}
