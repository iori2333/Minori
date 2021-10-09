package me.iori.minori.responders

import me.iori.minori.interfaces.Responder
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

class MemberEventResponder(channel: EventChannel<Event>) : Responder(channel) {
  override fun listen() {
    channel.subscribeAlways<GroupMemberEvent> {
      val message: Message = when (it) {
        is MemberLeaveEvent -> PlainText("有dalao离开了呢..")
        is MemberJoinEvent -> At(it.member) + "欢迎新椰叶入群"
        is MemberMuteEvent -> PlainText(
          "${it.member.nameCardOrNick}被万恶的" +
              (it.operator?.nameCardOrNick ?: bot.nameCardOrNick) +
              "禁言了${it.durationSeconds / 60}分钟"
        )
        is MemberUnmuteEvent -> PlainText(
          "${it.member.nameCardOrNick}被善良的" +
              "${it.operator?.nameCardOrNick ?: bot.nameCardOrNick}解除了禁言"
        )
//        is MemberCardChangeEvent -> PlainText("${it.origin}将群名片变更为了${it.new}")
        else -> return@subscribeAlways
      }
      group.sendMessage(message)
    }
  }
}
