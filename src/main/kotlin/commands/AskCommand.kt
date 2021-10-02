package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.LanguageData
import me.iori.minori.processors.UseInlines.parseInlines
import me.iori.minori.processors.UsePipelines
import me.iori.minori.utils.Recorder

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.toMessageChain
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
  override val usage = "(/)$primaryName    # $description"

  init {
    GlobalEventChannel.subscribeAlways<MessageEvent> {
      if (it.message.content.startsWith(primaryName)) {
        CommandManager.executeCommand(
          sender = toCommandSender(),
          command = this@AskCommand,
          arguments = message,
          checkPermission = true,
        )
      }
    }
  }

  override suspend fun CommandSender.onCommand(args: MessageChain) {
    val group = this.getGroupOrNull()
    val (text, pipelines) = preProcess(args.joinToString(" ") {
      it.toMessageChain().serializeToMiraiCode()
    })
    val trimmed = text.trim().removePrefix(primaryName)
    val send = postProcess(getResponse(group, trimmed), pipelines).parseInlines()
    if (send == trimmed || send.isEmpty()) {
      return
    }
    sendMessage(send.trim().deserializeMiraiCode())
  }

  private val tokens = listOf<Pair<Regex, (MatchResult) -> String>>(
    Regex("(.)不\\1得") to { randomChoiceSp(it) },
    Regex("(.)不\\1") to { randomChoice(it, "不") },
    Regex("(.)没\\1") to { randomChoice(it, "没") },
    Regex("多少") to { Random.nextInt(0, 100).toString() },
    Regex("哪里") to { randomPlace() },
    Regex("什么时候") to { "${Random.nextInt(24)}点${Random.nextInt(60)}分" },
    Regex("干什么") to { LanguageData.doings.random() },
    Regex("几") to { Random.nextInt(0, 10).toString() },
  )

  private val tokensWithGroup = listOf<Pair<Regex, (Group) -> String>>(
    Regex("谁") to { it.members.random().nameCardOrNick },
    Regex("为什么") to { "因为${randomContent(it)}，所以" },
    Regex("什么") to { randomContent(it) },
  )

  private fun getResponse(group: Group?, text: String): String {
    if (LanguageData.excludedPrefixes.any { text.startsWith(it) }) {
      return ""
    }
    var res = text.split("还是").random()
    tokens.forEach { res = res.replace(it.first, it.second) }
    if (group != null) {
      tokensWithGroup.forEach { res = res.replace(it.first) { _ -> it.second(group) } }
    }
    return res
  }

  private fun randomChoice(match: MatchResult, split: String) =
    if (Random.nextBoolean()) match.groupValues[1] else split + match.groupValues[1]

  private fun randomPlace(): String {
    val (place, suffixes) = LanguageData.places.entries.random()
    return place + suffixes.random()
  }

  private fun randomChoiceSp(match: MatchResult, split: String = "不"): String {
    if (LanguageData.excludedChoiceWords.contains(match.groupValues[1])) {
      return (if (Random.nextBoolean()) "" else split) + match.groupValues[1] + "得"
    }
    return match.groupValues[1] + if (Random.nextBoolean()) split else "得"
  }

  private fun randomContent(group: Group): String {
    return Recorder.randomMessage(group.id)
  }
}
