package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.MessageSQL
import me.iori.minori.utils.Addons.replaceAll
import me.iori.minori.utils.Recorder
import net.mamoe.mirai.console.command.*
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
  private val escapes = listOf(
    "&nbsp;" to " ",
    "\\n" to "\n",
  )

  @SubCommand
  @Description("返回群聊中最近几条消息")
  suspend fun MemberCommandSender.last(k: Int) = try {
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
  } catch (_: IllegalArgumentException) {
    sendMessage("请求消息参数不正确")
  }


  @SubCommand
  @Description("返回群聊某用户最近几条消息")
  suspend fun MemberCommandSender.last(user: User, k: Int) = try {
    val messages = Recorder.memberMessage(group.id, user.id, k)
    val forward = buildForwardMessage(group) {
      messages.forEach {
        add(it.sender, group[it.sender]?.nameCardOrNick ?: "${it.sender}", it.content.deserializeMiraiCode())
      }
    }
    sendMessage(forward)
  } catch (_: IllegalArgumentException) {
    sendMessage("请求消息参数不正确")
  }

  @SubCommand
  @Description("伪造聊天记录（单人）")
  suspend fun MemberCommandSender.build(user: User, vararg messages: SingleMessage) {
    val forward = buildForwardMessage(group) {
      messages.forEach {
        if (it is PlainText) {
          add(user.id, user.nameCardOrNick, it.replaceAll(escapes))
        } else {
          add(user.id, user.nameCardOrNick, it)
        }
      }
    }
    if (!forward.isContentEmpty()) {
      sendMessage(forward)
    }
  }

  @SubCommand
  @Description("伪造聊天记录（多人）")
  suspend fun MemberCommandSender.builds(vararg messages: SingleMessage) {
    var curr = bot.id
    var name = bot.nameCardOrNick
    val forward = buildForwardMessage(group) {
      messages.forEach {
        when (it) {
          is At -> {
            curr = it.target
            name = group[curr]?.nameCardOrNick ?: "$curr"
          }
          is PlainText -> add(curr, name, it.replaceAll(escapes))
          else -> add(curr, name, it)
        }
      }
    }
    if (!forward.isContentEmpty()) {
      sendMessage(forward)
    }
  }

  @SubCommand
  @Description("按关键词查询成员聊天记录")
  suspend fun MemberCommandSender.grep(user: User, message: MessageChain) {
    val selected = MessageSQL.select(group.id, user.id, message.serializeToMiraiCode())
    val count = selected.size
    if (count > 0) {
      val forward = buildForwardMessage(group) {
        selected.forEach {
          val sender = group[it.sender]
          add(
            senderId = it.sender,
            senderName = sender?.nameCardOrNick ?: bot.nameCardOrNick,
            message = it.content.deserializeMiraiCode()
          )
        }
      }
      sendMessage("${user.nameCardOrNick}说过${count}次这句话")
      sendMessage(forward)
    } else {
      sendMessage("${user.nameCardOrNick}没有说过这句话哦")
    }
  }
}
