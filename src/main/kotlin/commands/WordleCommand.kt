package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.WordleData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object WordleCommand : CompositeCommand(
  owner = Minori,
  primaryName = "wordle",
  description = "Play wordle game"
) {
  @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
  override val prefixOptional = true

  class GuessSession(private val answer: String) {
    private val trials: MutableList<Pair<String, String>> = mutableListOf()

    val length: Int = answer.length

    fun guess(word: String): Boolean {
      val ret = StringBuilder()
      for (i in word.indices) {
        if (word[i] == answer[i]) {
          ret.append("🟩")
        } else if (answer.contains(word[i])) {
          ret.append("🟨")
        } else {
          ret.append("⬛")
        }
      }
      trials.add(Pair(word, ret.toString()))
      return word == answer
    }

    fun count(): Int {
      return trials.size
    }

    fun stat(): String = trials.joinToString("\n") { (word, ans) -> "$word: $ans" }
  }

  private const val LIMITS = 6

  private val answers = mutableMapOf<Long, GuessSession>()

  @SubCommand
  @Description("Start a wordle game")
  suspend fun CommandSender.start(n: Int) {
    if (n < 4 || n > 10) {
      sendMessage("n should be in 4 ~ 10")
      return
    }
    val word = WordleData.words["l$n"]!!.random()
    println(word)

    val id = user?.id ?: 0
    if (answers.containsKey(id)) {
      sendMessage("A guess session has been started")
    } else {
      answers[id] = GuessSession(word)
    }
  }

  @SubCommand
  @Description("Guess answer")
  suspend fun CommandSender.w(word: String) {
    val id = user?.id ?: 0

    if (!answers.containsKey(id)) {
      sendMessage("There's no Wordle games")
      return
    }

    val session = answers[id]!!
    if (word.length != session.length || !WordleData.words["l${word.length}"]!!.contains(word)) {
      sendMessage("Not a valid guess")
      return
    }

    if (session.guess(word)) {
      sendMessage("Magnificent! (${session.count()}/$LIMITS)\nStat:\n${session.stat()}")
      answers.remove(id)
    } else if (session.count() <= LIMITS) {
      sendMessage("Current(${session.count()}/$LIMITS):\n${session.stat()}")
    } else {
      sendMessage("Limit exceeded.\nStat:\n${session.stat()}")
      answers.remove(id)
    }
  }
}
