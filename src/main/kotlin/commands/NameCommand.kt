package me.iori.minori.commands

import me.iori.minori.Minori
import me.iori.minori.data.LanguageData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

object NameCommand : SimpleCommand(
  owner = Minori,
  primaryName = "取名",
  description = "获取一个的高科技亚文化名字"
) {
  @Handler
  suspend fun CommandSender.onCommand() {
    var name = ""
    LanguageData.names.forEach { (_, u) -> name += u.random() }
    sendMessage("你好，$name")
  }
}
