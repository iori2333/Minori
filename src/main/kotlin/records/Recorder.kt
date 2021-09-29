package me.iori.minori.records

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.UserMessageEvent

object Recorder {
  init {
    GlobalEventChannel.subscribeAlways<GroupMessageEvent> { }
    GlobalEventChannel.subscribeAlways<UserMessageEvent> { }
  }
}
