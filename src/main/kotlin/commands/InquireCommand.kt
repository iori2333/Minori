package me.iori.minori.commands

import net.mamoe.mirai.console.command.MemberCommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.nameCardOrNick
import kotlin.random.Random

import me.iori.minori.Minori
import me.iori.minori.utils.Recorder
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object InquireCommand : SimpleCommand(
  owner = Minori,
  primaryName = "寻访群友",
  description = "对群友进行一次十连寻访"
) {
  private val stars = listOf(
    "★★★★★★      ",
    "★★★★★         ",
    "★★★★           ",
    "★★★              "
  )
  private val possibilities = listOf(0.02, 0.1, 0.6, 1.0)

  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true

  @Handler
  suspend fun MemberCommandSender.onCommand() {
    val samples = (1..10).map { group.members.random().nameCardOrNick }
    val store = mutableMapOf<String, String>()
    val result = samples.map {
      if (!store.containsKey(it)) {
        val index = possibilities.indexOfFirst { p -> p > Random.nextDouble() }
        store[it] = stars[index]
      }
      it to store[it] as String
    }

    val send = "「${Recorder.randomMessage(group.id)}」· 十连寻访\n" +
        result.joinToString("\n") { (name, stars) -> stars + name }
    sendMessage(send)
  }
}
