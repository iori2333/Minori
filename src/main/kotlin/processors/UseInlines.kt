package me.iori.minori.processors

import me.iori.minori.processors.inlines.*

object UseInlines {
  private val reg = Regex("\\\$\\(([^\$.]+?)\\)")

  private val inlines = listOf(
    RandInline,
    JoinInline,
    ChoiceInline,
    OptionalInline,
    RangeInline,
  )

  fun String.parseInlines(): String = try {
    var send = this
    while (reg.containsMatchIn(send)) {
      send = send.replace(reg) {
        val content = it.groupValues[1]
        val inline = inlines.first() { inline -> inline.match(content) }
        inline.parse(content)
      }
    }
    send
  } catch (_: NoSuchElementException) {
    "命令格式错误"
  }
}
