package me.iori.minori.interfaces

abstract class Pipeline(val name: String) {
  abstract fun execute(text: String, params: String): String
}

