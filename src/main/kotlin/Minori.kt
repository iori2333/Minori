package me.iori.minori

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.event.events.GroupMemberEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel

import me.iori.minori.commands.*
import me.iori.minori.commands.simple.*
import me.iori.minori.data.*
import me.iori.minori.response.*
import me.iori.minori.utils.*

object Minori : KotlinPlugin(JvmPluginDescription("me.iori.minori", "0.2") {
  name("Minori")
  info("Minori Bot")
  author("Iori")
}) {
  private lateinit var commands: List<Command>
  private lateinit var data: List<PluginData>

  override fun onEnable() {
    val channel = globalEventChannel(coroutineContext)

    val responders = listOf(
      MessageResponder(channel.filter { it is MessageEvent }),
      MemberEventResponder(channel.filter { it is GroupMemberEvent }),
    )

    responders.forEach { it.listen() }

    commands = listOf(
      AskCommand,
      LuckCommand,
      PingCommand,
      LogCommand,
      SetuCommand,
      EatCommand,
      LearnCommand,
      InquireCommand,
    )
    data = listOf(LanguageData, MessageCache, ResponsesData)
    Recorder.listen()
    Network.load()

    data.forEach { it.reload() }
    commands.forEach { it.register() }
  }

  override fun onDisable() {
    Recorder.dispose()
    Network.dispose()

    commands.forEach { it.unregister() }
  }
}
