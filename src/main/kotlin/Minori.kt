package me.iori.minori

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

import me.iori.minori.commands.*
import me.iori.minori.data.*
import me.iori.minori.records.Recorder
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.data.PluginData

object Minori : KotlinPlugin(JvmPluginDescription("me.iori.minori", "0.1") {
  name("Minori")
  info("Minori Bot")
  author("Iori")
}) {
  private lateinit var commands: List<Command>
  private lateinit var data: List<PluginData>

  override fun onEnable() {
    commands = listOf(AskCommand, LuckCommand, PingCommand, LogCommand)
    data = listOf(LanguageData, MessageCache)
    Recorder.listen()

    data.forEach { it.reload() }
    commands.forEach { it.register() }
  }

  override fun onDisable() {
    Recorder.dispose()

    commands.forEach { it.unregister() }
  }
}
