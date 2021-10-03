package me.iori.minori.commands

import me.iori.minori.Minori
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import kotlin.math.ceil

object CalcCommand : CompositeCommand(
  owner = Minori,
  primaryName = "calc",
  description = "计算音游控分方法"
) {
  @OptIn(
    ConsoleExperimentalApi::class,
    net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors::class
  )
  override val prefixOptional = true

  @SubCommand("dynamix", "dy")
  @Description("计算Dynamix控分需求")
  suspend fun CommandSender.dynamix(target: Int, fullCombos: Int) {
    val ap = 1000000
    if (target < 0 || target > ap) {
      sendMessage("分数不在可能范围")
    }
    val singlePerfect = (ap.toDouble() - 80000) / fullCombos
    val singleGreat = singlePerfect * 0.6
    for (perfect in 0..fullCombos) {
      for (good in 0..(fullCombos - perfect)) {
        val miss = fullCombos - perfect - good
        val minCombo = ceil((fullCombos - miss).toDouble() / (miss + 1)).toInt()
        val maxCombo = perfect + good
        for (combo in minCombo..maxCombo) {
          val res = singlePerfect * perfect + singleGreat * good + 80000 * combo / fullCombos
          if (res.toInt() == target) {
            sendMessage("您需要打出${perfect}个Perfect，${good}个Good，${miss}个Miss，并保持Max Combo为${combo}")
            return
          }
        }
      }
    }
    sendMessage("没有找到可行解")
  }

  @SubCommand("arcaea", "arc")
  @Description("计算Arcaea控分需求")
  suspend fun CommandSender.arcaea(target: Int, fullRecalls: Int) {
    val pm = 10000000
    val mpm = pm + fullRecalls
    if (target < 0 || target > mpm) {
      sendMessage("分数不在可能范围")
    }
    val singlePure = pm.toDouble() / fullRecalls
    val singleFar = singlePure / 2
    for (maxPure in 0..fullRecalls) {
      for (pure in 0..(fullRecalls - maxPure)) {
        for (far in 0..(fullRecalls - maxPure - pure)) {
          if ((singlePure * (maxPure + pure) + singleFar * far + maxPure).toInt() == target) {
            sendMessage(
              "您需要打出${maxPure}个大P，${pure}个小P以及${far}个Far，" +
                  "${fullRecalls - maxPure - pure - far}个Lost"
            )
            return
          }
        }
      }
    }
    sendMessage("没有找到可行解")
  }
}
