package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.AskData
import me.iori.minori.processors.UsePipelines

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import kotlin.random.Random

@OptIn(
  ConsoleExperimentalApi::class,
  net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
)
object AskCommand : RawCommand(
  owner = Minori,
  primaryName = "问",
  description = "让Minori回答问题",
), UsePipelines {
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
    val group = this.getGroupOrNull()
    val (text, pipelines) = preProcess(args.content)
    val send = postProcess(getResponse(group, text), pipelines)
    sendMessage(send.deserializeMiraiCode())
  }

  private val tokenTable = listOf<Pair<Regex, (MatchResult) -> String>>(
    Pair(Regex("(.)不\\1得")) { randomChoiceSp(it) },
    Pair(Regex("(.)不\\1")) { randomChoice(it, "不") },
    Pair(Regex("(.)没\\1")) { randomChoice(it, "没") },
    Pair(Regex("多少")) { Random.nextInt(0, 100).toString() },
    Pair(Regex("哪里")) { randomPlace() },
    Pair(Regex("什么时候")) { "${Random.nextInt(24)}点${Random.nextInt(60)}分" },
    Pair(Regex("干什么")) { AskData.doings.random() },
    Pair(Regex("几")) { Random.nextInt(0, 10).toString() },
  )

  private fun getResponse(group: Group?, text: String): String {
    if (AskData.excludedPrefixes.any { text.startsWith(it) }) {
      return ""
    }

    var res = text.substring(1).split("还是").random()
    tokenTable.forEach { res = res.replace(it.first, transform = it.second) }
    if (group != null) {
      res = res.replace(Regex("谁")) { group.members.random().nameCardOrNick }
      res = res.replace(Regex("什么")) { randomContent(group) }
    }
    return res
  }

  private fun randomChoice(match: MatchResult, split: String) =
    if (Random.nextBoolean()) match.groupValues[1] else split + match.groupValues[1]

  private fun randomPlace(): String {
    val (place, suffixes) = AskData.places.entries.random()
    return place + suffixes.random()
  }

  private fun randomChoiceSp(match: MatchResult, split: String = "不"): String {
    if (AskData.excludedChoiceWords.contains(match.groupValues[1])) {
      return (if (Random.nextBoolean()) "" else split) + match.groupValues[1] + "得"
    }
    return match.groupValues[1] + if (Random.nextBoolean()) split else "得"
  }

  private fun randomContent(group: Group): String {
    return "pong!"
  }
}
