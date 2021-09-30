package me.iori.minori

import io.ktor.util.date.*
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random

fun main() {
  val l = listOf(1, 2, 3, 4, 5, 6)
  println(l.random(Random(114514)))
}
