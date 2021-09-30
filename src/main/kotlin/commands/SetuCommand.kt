package me.iori.minori.commands

import java.io.File
import io.ktor.client.request.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content

import me.iori.minori.Minori
import me.iori.minori.utils.Network

object SetuCommand : RawCommand(
  owner = Minori,
  description = "获取色图",
  primaryName = "色图"
) {
  private const val URL = "https://api.lolicon.app/setu/v2"

  @Serializable
  data class Picture(
    val urls: Map<String, String>
  )

  @Serializable
  data class Response(
    val error: String,
    val data: List<Picture>,
  )

  override suspend fun CommandSender.onCommand(args: MessageChain) {
    val tags = if (args.isEmpty()) listOf() else args.map { it.content }
    val r: Response = Network.get(URL) {
      parameter("r18", 0)
      if (tags.isNotEmpty()) parameter("tag", tags.joinToString("|"))
    }

    if (r.data.isEmpty()) {
      sendMessage("没有找到相关色图")
      return
    }
    val urlString = r.data[0].urls["original"]
    if (urlString == null) {
      sendMessage("没有找到相关色图")
      return
    }
    try {
      val rawImage = Network.get<ByteArray>(urlString)
      val file = File("image.jpg")

      withContext(Dispatchers.IO) {
        @Suppress("BlockingMethodInNonBlockingContext") // 麻了
        file.outputStream().write(rawImage)
      }
      subject?.sendImage(rawImage.inputStream())
    } catch (_: ConnectTimeoutException) {
      sendMessage("网路发生了异常，色图的链接为$urlString")
    }
  }
}
