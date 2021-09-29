package me.iori.minori.processors

import net.mamoe.mirai.message.data.MessageChain

interface UsePreProcessor {
  fun preProcess(text: String): String {
    return text
  }

  fun preProcess(args: MessageChain): MessageChain {
    return args
  }
}
