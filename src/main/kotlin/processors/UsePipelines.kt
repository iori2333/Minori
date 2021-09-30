package me.iori.minori.processors

import me.iori.minori.processors.pipelines.*

interface UsePipelines {
  fun preProcess(text: String): Pair<String, List<Pair<Pipeline, String>>> {
    val segments = text.split("|").map { it.trim() }
    val ret = segments.first()

    val preprocessed = segments.subList(1, segments.size)
    return try {
      Pair(ret, preprocessed.map {
        pipelines.firstNotNullOfOrNull { (key, value) ->
          if (it.startsWith(key)) Pair(value, it) else null
        } ?: throw NoSuchMethodError()
      })
    } catch (_: NoSuchMethodError) {
      Pair(ret, listOf())
    }
  }

  fun postProcess(text: String, pipelines: List<Pair<Pipeline, String>>): String {
    var send = text
    pipelines.map { (pipeline, params) -> send = pipeline.execute(send, params) }
    return send
  }
}
