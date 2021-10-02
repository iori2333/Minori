package me.iori.minori.processors.inlines

abstract class InlineCommand(
  protected val name: String,
  protected val reg: Regex,
) {
  abstract fun parse(text: String): String

  fun match(text: String): Boolean {
    return reg.matches(text)
  }
}
