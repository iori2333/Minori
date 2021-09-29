package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.processors.UsePostProcessor
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content

@OptIn(
  ConsoleExperimentalApi::class,
  net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
)
object AskCommand : RawCommand(
  owner = Minori,
  primaryName = "问",
  description = "让Minori回答问题",
), UsePostProcessor {
  init {
    GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
      if (it.message.content.startsWith(primaryName)) {
        CommandManager.executeCommand(
          sender = it.toCommandSender(),
          command = this@AskCommand,
          arguments = message,
          checkPermission = true,
        )
      }
    }
  }

  override suspend fun CommandSender.onCommand(args: MessageChain) {
    val group = this.getGroupOrNull() ?: return
    val send = getResponse(group, args)

    sendMessage(postProcess(send))
  }

  private suspend fun getResponse(group: Group, args: MessageChain): String {
    return "pong!"
  }
}
