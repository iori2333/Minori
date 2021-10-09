package me.iori.minori.processors.pipelines

import me.iori.minori.interfaces.Pipeline

object GrepPipeline : Pipeline("grep") {
  private const val GREP_START = "<<"
  private const val GREP_END = ">>"

  override fun execute(text: String, params: String): String {
    val keyword = params.removePrefix(name).trim()
    return text.replace(keyword, GREP_START + keyword + GREP_END)
  }
}
