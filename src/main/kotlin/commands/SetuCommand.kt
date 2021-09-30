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
import kotlin.random.Random

object SetuCommand : RawCommand(
  owner = Minori,
  description = "获取色图",
  primaryName = "色图",
  prefixOptional = true,
  usage = "(/)色图 <tags?>    # 根据标签获取色图"
) {
  private const val URL = "https://api.lolicon.app/setu/v2"
  private const val QUALITY = "original"
  private const val STORAGE_PATH = "data/Minori/setus"

  @Serializable
  data class Picture(
    val pid: Int,
    val uid: Int,
    val ext: String,
    val urls: Map<String, String>
  )

  @Serializable
  data class Response(
    val error: String,
    val data: List<Picture>,
  )

  override suspend fun CommandSender.onCommand(args: MessageChain) {
    val tags = if (args.isEmpty()) listOf() else args.map { it.content }
    val r: Response = Network.json(URL) {
      parameter("r18", 0)
      parameter("size", QUALITY)
      if (tags.isNotEmpty()) parameter("tag", tags.joinToString("|"))
    }

    if (r.data.isEmpty()) {
      sendMessage("没有找到相关色图")
      return
    }

    if (r.error.isNotEmpty()) {
      sendMessage("色图获取失败：${r.error}")
      return
    }

    val img = r.data.first()
    val urlString = img.urls[QUALITY] as String
    try {
      val rawImage = Network.get<ByteArray>(urlString)
      val file = File("$STORAGE_PATH/${img.pid}${img.uid}.${img.ext}")

      withContext(Dispatchers.IO) {
        @Suppress("BlockingMethodInNonBlockingContext") // 麻了
        file.outputStream().write(rawImage)
      }
      subject?.sendImage(rawImage.inputStream())
        ?.recallIn((Random.nextInt(20, 40) * 1000).toLong())
    } catch (_: ConnectTimeoutException) {
      sendMessage("网路发生了异常，色图的链接为$urlString")
        ?.recallIn((Random.nextInt(20, 40) * 1000).toLong())
    }
  }
}
