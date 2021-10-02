package me.iori.minori.processors.inlines

object RangeInline : InlineCommand(
  "range",
  Regex("range (\\d+) (\\d+)")
) {
  override fun parse(text: String): String {
    val match = reg.matchEntire(text) as MatchResult
    val (left, right) = match.groupValues.subList(1, 3).map { it.toInt() }
    return (left until right).joinToString(" ")
  }
}
