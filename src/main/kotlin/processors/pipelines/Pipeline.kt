package me.iori.minori.processors.pipelines

abstract class Pipeline(val name: String) {
  abstract fun execute(text: String, params: String): String
}

