package me.iori.minori.commands

import me.iori.minori.Minori
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object PingCommand : SimpleCommand(
  owner = Minori,
  primaryName = "ping",
  description = "ping & pong!"
) {
  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true
  private val responses = listOf("Pong!", "Zzz...", "我还在睡喵...")

  @Handler
  suspend fun CommandSender.handle() {
    sendMessage(responses.random())
  }
}
