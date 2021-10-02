package me.iori.minori.response

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel

abstract class Responder(
  protected val channel: EventChannel<Event>
) {
  abstract fun listen()
}
