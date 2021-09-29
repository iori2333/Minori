package me.iori.minori.processors

import net.mamoe.mirai.message.data.MessageChain

interface UsePostProcessor {
  fun postProcess(text: String): String {
    return text
  }

  fun posProcess(args: MessageChain): MessageChain {
    return args
  }
}
