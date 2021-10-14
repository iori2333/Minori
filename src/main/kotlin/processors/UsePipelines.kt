package me.iori.minori.processors

import me.iori.minori.interfaces.Pipeline
import me.iori.minori.processors.pipelines.*

object UsePipelines {
  private val pipelines = mapOf<String, Pipeline>(
    "grep" to GrepPipeline
  )

  fun String.toPipelines(): Pair<String, List<Pair<Pipeline, String>>> {
    val segments = this.split("|").map { it.trim() }
    val ret = segments.first()

    val preprocessed = segments.subList(1, segments.size)
    return try {
      Pair(ret, preprocessed.map {
        pipelines.firstNotNullOf { (key, value) ->
          if (it.startsWith(key)) Pair(value, it) else null
        }
      })
    } catch (_: NoSuchElementException) {
      Pair(ret, listOf())
    }
  }

  fun String.parsePipelines(pipelines: List<Pair<Pipeline, String>>): String {
    var send = this
    pipelines.forEach { (pipeline, params) -> send = pipeline.execute(send, params) }
    return send
  }

  fun String.pipelining(): String {
    val (text, pipelines) = this.toPipelines()
    return text.parsePipelines(pipelines)
  }
}
