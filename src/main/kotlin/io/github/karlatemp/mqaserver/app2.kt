package io.github.karlatemp.mqaserver

import io.github.karlatemp.mqaserver.protocol.packet.JoinGame
import io.github.karlatemp.mqaserver.utils.ServerPing
import io.github.karlatemp.mqaserver.utils.Util
import kotlinx.coroutines.Job
import net.md_5.bungee.api.chat.TextComponent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

fun main() {
    Bootstrap._addr = Util.getAddr("0.0.0.0")
    Bootstrap.onlineMode = true
    Bootstrap.worldType = JoinGame.WorldType.WORLD_THE_END
    Bootstrap.defaultLocation = doubleArrayOf(0.0, 64.0, 0.0)
    Bootstrap.serverPing = { protocolVersion ->
        ServerPing(
            ServerPing.Protocol("RBQ", protocolVersion),
            ServerPing.Players(1919810, 114514, arrayOf()),
            TextComponent.fromLegacyText("§b贺兰§cRBQ!").component,
            null
        )
    }
    setupApplication { player ->
        player.updateServerName("贺兰 RBQ")
        player.sendActionBar(*TextComponent.fromLegacyText("Action §bBar~~~§6 TEST!!!"))
        player.sendMessage("打开以下网址获取新的RBQ, 贺兰限定款")
        player.sendMessage(
            "https://ilove.sb.helan.forever.com/HeLanRBQ?key=${
                UUID.randomUUID().toString().replace("-", "")
            }"
        )
        val formatter = SimpleDateFormat("HH:mm:ss")
        while (coroutineContext[Job]?.isActive == true) {
            val next = player.receiveMessageOrNull()
            if (next == null) {
                println("BYE")
                println("TEST QUIT NIL> " + player.receiveMessageOrNull())
                break
            } else {
                player.sendMessage("[${formatter.format(Date())}] $next")
            }
        }
        println("Quited.")
    }
}
