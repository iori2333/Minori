package me.iori.minori.processors.inlines

import kotlin.random.Random

object OptionalInline : InlineCommand(
  "optional",
  Regex("optional (.*)")
) {
  override fun parse(text: String): String {
    val input = reg.matchEntire(text)?.groupValues?.get(1) ?: ""
    return if (Random.nextBoolean()) {
      input
    } else {
      ""
    }
  }
}
