package me.iori.minori

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

import me.iori.minori.commands.*
import me.iori.minori.data.*

object Minori : KotlinPlugin(JvmPluginDescription("me.iori.minori", "0.1") {
  name("Minori")
  info("Minori Bot")
  author("Iori")
}) {
  override fun onEnable() {
    AskData.reload()

    AskCommand.register()
    PingCommand.register()
  }

  override fun onDisable() {
    AskCommand.unregister()
    PingCommand.unregister()
  }
}
