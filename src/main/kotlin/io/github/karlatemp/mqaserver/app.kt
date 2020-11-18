package io.github.karlatemp.mqaserver

import io.github.karlatemp.mqaserver.protocol.packet.JoinGame
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.md_5.bungee.api.ChatColor

fun main() {
    setupApplication { player ->
        coroutineScope {
            player.sendMessage("IK" + player.ask {
                sendMessage("随便输入什么东西")
            })
            player.sendMessage("输入一个你喜欢的名字, 文本, 什么都行")
            val serverName = player.receiveMessage()
            player.sendMessage("你输入的是: $serverName")
            player.updateServerName(serverName)
            rp@ while (true) {
                val msg = player.receiveMessageOrNull() ?: break@rp
                player.sendMessage("> $msg")
                when (msg.substringBefore(' ')) {
                    "rgb" -> launch {
                        var w = 1
                        while (true) {
                            w++
                            val kw = w % serverName.length
                            player.updateServerName(
                                serverName.insert(
                                    if (w % (2 * serverName.length) != kw) {
                                        serverName.length - kw
                                    } else kw, "\u00a7b",
                                    suf = "\u00a7r"
                                )
                            )
                            delay(50)
                        }
                    }
                    "tp" -> {
                        val loc3d = msg.substringAfter(' ').split(' ')
                            .map { it.toDoubleOrNull() }
                        if (loc3d.none { it == null } && loc3d.size == 3) {
                            @Suppress("UNCHECKED_CAST")
                            val mapped = loc3d as List<Double>
                            player.teleport(
                                mapped[0], mapped[1], mapped[2]
                            )
                        }
                    }
                    "server" -> {
                        player.updateServerName(msg.substringAfter(' '))
                    }
                    "dis" -> {
                        player.disconnect(
                            ChatColor.translateAlternateColorCodes(
                                '&', msg.substringAfter(' ', "")
                            )
                        )
                    }
                    /*"world" -> {
                        kotlin.runCatching {
                            player.changeWorld(JoinGame.WorldType.valueOf(msg.substringAfter(' ', "")))
                        }
                    }*/
                }
            }
        }
    }
}