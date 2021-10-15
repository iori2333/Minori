pluginManagement {
  val kotlinVersion: String by settings
  val miraiVersion: String by settings

  plugins {
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version miraiVersion
  }

  repositories {
    mavenLocal()
    gradlePluginPortal()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
  }
}

rootProject.name = "minori"
