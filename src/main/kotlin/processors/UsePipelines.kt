package me.iori.minori.processors

import me.iori.minori.processors.pipelines.Pipeline

interface UsePipelines {
  fun preProcess(text: String): Pair<String, List<Pipeline>> {
    return Pair(text, listOf())
  }

  fun postProcess(text: String, pipelines: List<Pipeline>): String {
    return text
  }
}
