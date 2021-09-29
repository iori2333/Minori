package me.iori.minori

import me.iori.minori.configs.Constants
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
suspend fun main() {
  MiraiConsoleTerminalLoader.startAsDaemon()
  Minori.load()
  Minori.enable()

  MiraiConsole
    .addBot(Constants.BOT_QQ, Constants.BOT_PASSWORD) { fileBasedDeviceInfo() }
    .alsoLogin()

  MiraiConsole.job.join()
}
