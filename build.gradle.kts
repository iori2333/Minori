plugins {
  val kotlinVersion = "1.5.10"

  kotlin("jvm") version kotlinVersion
  kotlin("plugin.serialization") version kotlinVersion
  id("net.mamoe.mirai-console") version "2.7.1"
}

dependencies {
  api("net.mamoe:mirai-console-terminal:2.7.1") // 自行替换版本
  api("net.mamoe:mirai-core:2.7.1")
}

group = "me.iori"
version = "0.1.0"

repositories {
  mavenLocal()
  maven("https://maven.aliyun.com/repository/public")
  mavenCentral()
}

kotlin.sourceSets.all { languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn") }
