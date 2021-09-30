package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.LanguageData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.nameCardOrNick
import java.util.*
import kotlin.random.Random

object LuckCommand : SimpleCommand(
  owner = Minori,
  primaryName = "今日运势",
  description = "使用玄学方法计算今日运势"
) {
  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true

  @Handler
  suspend fun CommandSender.onCommand() {
    val calendar = Calendar.getInstance()
    calendar.set(
      calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.DATE),
      0, 0, 0
    )
    val seed = calendar.timeInMillis / 1000 + (user?.id ?: 0)
    val random = Random(seed)
    val rank = LanguageData.ranks.random(random)
    val shuffled = LanguageData.events.shuffled(random).subList(0, 6)

    val send = """
      ${user?.nameCardOrNick ?: user?.id ?: 0}的今日运势：
      $rank
      宜：${shuffled.subList(0, 3).joinToString("、")}
      忌：${shuffled.subList(3, 6).joinToString("、")}
    """.trimIndent()

    sendMessage(send)
  }
}
