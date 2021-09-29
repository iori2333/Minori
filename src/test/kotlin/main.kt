package me.iori.minori

import kotlinx.coroutines.runBlocking
import me.iori.minori.configs.Constants
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
fun main() = runBlocking {
  MiraiConsoleTerminalLoader.startAsDaemon()

  MiraiConsole.addBot(Constants.BOT_QQ, Constants.BOT_PASSWORD).alsoLogin()

  Minori.load()
  Minori.enable()

  MiraiConsole.job.join()
}
