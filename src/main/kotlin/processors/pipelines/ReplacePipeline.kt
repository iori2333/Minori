package me.iori.minori.processors.pipelines

import me.iori.minori.interfaces.Pipeline

object ReplacePipeline : Pipeline("replace") {
  override fun execute(text: String, params: String): String = try {
    val (from, to) = params.split(" ", limit = 2)
    text.replace(from.toRegex(), to)
  } catch (_: Throwable) {
    text
  }
}
