package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.utils.Recorder
import net.mamoe.mirai.console.command.MemberCommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.nameCardOrNick

object PoemCommand : SimpleCommand(
  owner = Minori,
  primaryName = "作诗",
  description = "根据已有语料作诗",
) {
  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true

  private val templates = listOf(
    "%s，%s。\n%s，%s。\n%s，%s。\n%s，%s。",
    "%s，%s。\n%s，%s？\n%s，%s，%s。\n%s，%s，%s！"
  )

  private fun buildPoem(title: String, poet: String, content: String): String =
    "《$title》\n$poet\n$content"

  @Handler
  suspend fun MemberCommandSender.onCommand() {
    val poem = templates.random().replace(Regex("%s")) { Recorder.randomMessage(group.id) }
    val send = buildPoem(Recorder.randomMessage(group.id), group.members.random().nameCardOrNick, poem)
    sendMessage(send)
  }
}
