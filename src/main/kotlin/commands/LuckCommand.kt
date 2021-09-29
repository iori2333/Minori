package me.iori.minori.commands

import me.iori.minori.Minori
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object LuckCommand : SimpleCommand(
  owner = Minori,
  primaryName = "今日运势",
  description = "使用玄学方法计算今日运势"
) {
  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true

  @Handler
  suspend fun CommandSender.onCommand() {
    sendMessage("今日运势")
  }
}
