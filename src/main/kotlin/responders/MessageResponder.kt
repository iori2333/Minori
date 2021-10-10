package me.iori.minori.responders

import io.ktor.client.request.*
import me.iori.minori.commands.AskCommand
import me.iori.minori.data.ResponsesData
import me.iori.minori.interfaces.Responder
import me.iori.minori.utils.Addons.toRecord
import me.iori.minori.utils.Network
import me.iori.minori.utils.Recorder
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import kotlin.random.Random

@OptIn(
  ExperimentalCommandDescriptors::class,
  net.mamoe.mirai.console.util.ConsoleExperimentalApi::class
)
class MessageResponder(
  channel: EventChannel<Event>,
  private val prob: Double = 0.3,
) : Responder(channel) {
  private suspend fun arcHandler(im: Image): String = try {
    val res = Network.json<Map<String, Double>>("http://127.0.0.1:5000/arc") {
      parameter("img", im.queryUrl())
    }
    if (res.containsKey("ex")) {
      "您！！"
    } else if (res.containsKey("aa")) {
      "瞎了"
    } else {
      ""
    }
  } catch (_: Exception) {
    ""
  }

  override fun listen() {
    channel.subscribeMessages {
      always {
        Recorder.record(toRecord())
        val msg = message.last()
        if (msg is Image) {
          val send = arcHandler(msg)
          if (send.isNotEmpty()) {
            subject.sendMessage(send)
          }
        }
      }

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
        contains(key) {
          if (Random.nextDouble() < prob)
            subject.sendMessage(res.random())
        }
      }
    }
  }
}
