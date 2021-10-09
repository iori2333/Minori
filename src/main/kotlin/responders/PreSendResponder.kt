package me.iori.minori.responders

import me.iori.minori.interfaces.Responder
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.MessagePreSendEvent
import net.mamoe.mirai.message.data.isContentEmpty

class PreSendResponder(channel: EventChannel<Event>) : Responder(channel) {
  override fun listen() {
    channel.subscribeAlways<MessagePreSendEvent> {
      if (it.message.isContentEmpty()) {
        it.cancel()
        return@subscribeAlways
      }
    }
  }
}
