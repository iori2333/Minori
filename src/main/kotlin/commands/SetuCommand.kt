package me.iori.minori.commands

import io.ktor.client.*
import me.iori.minori.Minori
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content

object SetuCommand : RawCommand(
  owner = Minori,
  description = "获取色图",
  primaryName = "色图"
) {
  override suspend fun CommandSender.onCommand(args: MessageChain) {
    if (args.isEmpty()) {
      sendMessage("empty setu")
      return
    }
    val tags = args.map { it.content }
    println(tags.joinToString())
  }
}
