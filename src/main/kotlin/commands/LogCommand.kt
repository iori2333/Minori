package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.records.Recorder
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.getBotOrNull
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.*

object LogCommand : CompositeCommand(
  owner = Minori,
  primaryName = "log",
  description = "查看聊天记录",
) {
  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true

  @SubCommand
  @Description("返回群聊中最近几条消息")
  suspend fun CommandSender.last(k: Int) {
    val group = this.getGroupOrNull() ?: return

    val messages = Recorder.recentMessage(group.id, k)
    val forward = buildForwardMessage(group) {
      messages.forEach {
        add(
          it.sender,
          group[it.sender]?.nameCardOrNick ?: "${it.sender}",
          it.content.deserializeMiraiCode()
        )
      }
    }
    sendMessage(forward)
  }

  @SubCommand
  @Description("返回群聊某用户最近几条消息")
  suspend fun CommandSender.last(user: User, k: Int) {
    val group = this.getGroupOrNull() ?: return
    val messages = Recorder.memberMessage(group.id, user.id, k)
    val forward = buildForwardMessage(group) {
      messages.forEach {
        add(it.sender, group[it.sender]?.nameCardOrNick ?: "${it.sender}", it.content.deserializeMiraiCode())
      }
    }
    sendMessage(forward)
  }

  @SubCommand
  @Description("伪造聊天记录（单人）")
  suspend fun CommandSender.build(user: User, vararg messages: Message) {
    val group = this.getGroupOrNull() ?: return
    val forward = buildForwardMessage(group) {
      messages.forEach {
        add(user.id, user.nameCardOrNick, it)
      }
    }
    if (!forward.isContentEmpty()) {
      sendMessage(forward)
    }
  }

  @SubCommand
  @Description("伪造聊天记录（多人）")
  suspend fun CommandSender.builds(vararg messages: SingleMessage) {
    val group = this.getGroupOrNull() ?: return
    val bot = this.getBotOrNull() ?: return
    var curr = bot.id
    var name = bot.nameCardOrNick
    val forward = buildForwardMessage(group) {
      messages.forEach {
        if (it is At) {
          curr = it.target
          name = group[curr]?.nameCardOrNick ?: "$curr"
        } else {
          add(curr, name, it)
        }
      }
    }
    if (!forward.isContentEmpty()) {
      sendMessage(forward)
    }
  }
}
