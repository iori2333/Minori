package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.WordleData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText

object WordleCommand : CompositeCommand(
  owner = Minori,
  primaryName = "wordle",
  description = "Play wordle game"
) {
  @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
  override val prefixOptional = true

  class GuessSession(val answer: String) {
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

  private val sessions = mutableMapOf<Long, GuessSession>()

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
    val prefix = if (getGroupOrNull() == null) {
      PlainText("")
    } else {
      At(id)
    }

    if (sessions.containsKey(id)) {
      sendMessage(prefix + "A guess session has been started")
    } else {
      sessions[id] = GuessSession(word)
      sendMessage(prefix + "Wordle game started!")
    }
  }

  @SubCommand
  @Description("Guess answer")
  suspend fun CommandSender.w(word: String) {
    val id = user?.id ?: 0
    val prefix = if (getGroupOrNull() == null) {
      PlainText("")
    } else {
      At(id)
    }

    if (!sessions.containsKey(id)) {
      sendMessage(prefix + "There's no Wordle games")
      return
    }

    val session = sessions[id]!!
    if (word.length != session.length || !WordleData.words["l${word.length}"]!!.contains(word)) {
      sendMessage(prefix + "Not a valid guess")
      return
    }

    if (session.guess(word)) {
      sendMessage(prefix + "Magnificent! (${session.count()}/$LIMITS)\nStat:\n${session.stat()}")
      sessions.remove(id)
    } else if (session.count() < LIMITS) {
      sendMessage(prefix + "Current(${session.count()}/$LIMITS):\n${session.stat()}")
    } else {
      sendMessage(prefix + "Limit exceeded. Answer: ${session.answer}\nStat:\n${session.stat()}")
      sessions.remove(id)
    }
  }
}
