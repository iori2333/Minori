package me.iori.minori.response

import me.iori.minori.data.ResponsesData
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.subscribeMessages

class MessageResponder(channel: EventChannel<Event>) : Responder(channel) {
  override fun listen() {
    channel.subscribeMessages {
      ResponsesData.responses.forEach { (key, res) ->
        contains(key) reply res.random()
      }
    }
  }
}
