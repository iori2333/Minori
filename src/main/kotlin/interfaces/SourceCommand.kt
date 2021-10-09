package me.iori.minori.interfaces

import me.iori.minori.Minori
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(
  ConsoleExperimentalApi::class,
  net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
)
abstract class SourceCommand(
  name: String,
  description: String,
  protected val entries: List<String>,
  protected val formats: List<String> = listOf(),
  override val prefixOptional: Boolean = true,
) : SimpleCommand(
  owner = Minori,
  primaryName = name,
  description = description
) {
  @Handler
  open suspend fun CommandSender.onCommand() {
    if (formats.isEmpty()) {
      sendMessage(entries.random())
    } else {
      sendMessage(formats.random().format(entries.random()))
    }
  }
}
