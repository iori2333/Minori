package me.iori.minori.responders

import me.iori.minori.commands.AskCommand
import me.iori.minori.data.ResponsesData
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.subscribeMessages
import kotlin.random.Random

class MessageResponder(channel: EventChannel<Event>) : Responder(channel) {
  private val prob = 0.5

  @OptIn(
    ExperimentalCommandDescriptors::class,
    net.mamoe.mirai.console.util.ConsoleExperimentalApi::class
  )
  override fun listen() {
    channel.subscribeMessages {
      startsWith(
        prefix = "问",
        removePrefix = false,
      ) {
        CommandManager.executeCommand(
          toCommandSender(),
          AskCommand,
          message,
          checkPermission = true
        )
      }
      ResponsesData.responses.forEach { (key, res) ->
        contains(key) reply if (Random.nextDouble() < prob) res.random() else ""
      }
    }
  }
}
