package io.github.karlatemp.mqaserver.impl

import io.github.karlatemp.mqaserver.Bootstrap
import io.github.karlatemp.mqaserver.MessageHandler
import io.github.karlatemp.mqaserver.core.PlayerImpl
import io.github.karlatemp.mqaserver.handlers.UpstreamBridge
import io.github.karlatemp.mqaserver.netty.ChannelWrapper
import io.github.karlatemp.mqaserver.protocol.DefinedPacket
import io.github.karlatemp.mqaserver.protocol.PacketWrapper
import io.github.karlatemp.mqaserver.protocol.packet.Chat
import io.github.karlatemp.mqaserver.protocol.packet.JoinGame
import io.github.karlatemp.mqaserver.protocol.packet.PluginMessage
import io.github.karlatemp.mqaserver.protocol.packet.Teleport
import io.netty.buffer.Unpooled
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class UBImpl(val player: PlayerImpl) : UpstreamBridge(player) {
    val messageReceiveQueue = ConcurrentLinkedQueue<Continuation<String?>>()
    val hookedJobs = ConcurrentLinkedQueue<Job>()
    var onMessage: MessageHandler? = null
    var onMessageNotReceived: MessageHandler? = null

    private val msgLock = ReentrantLock()

    private var disconnected = false

    init {
        PlayerManager.putConnection(player)
        player.extData = this
        Bootstrap.application?.run {
            hookedJobs.add(Bootstrap.scope.launch(SupervisorJob()) {
                onPlayerConnect(player)
            })
        }
    }

    override fun shouldHandle(packet: PacketWrapper): Boolean {
        return packet.packet is Chat
    }

    override fun handle(chat: Chat) {
        onMessage?.invoke(chat.message)

        val task = messageReceiveQueue.poll()
        if (task != null) {
            task.resume(chat.message)
        } else {
            onMessageNotReceived?.invoke(chat.message)
        }
    }

    override fun setup() {
        writePacket(JoinGame())
        writePacket(Teleport())
    }

    fun updateServerName(name: String) {
        writePacket(
            PluginMessage(
                "minecraft:brand",
                Unpooled.buffer()
                    .also {
                        DefinedPacket.writeString(name, it)
                    }
                    .let {
                        val result = ByteArray(it.readableBytes())
                        it.readBytes(result)
                        result
                    },
                false
            )
        )
    }

    override fun disconnected(channel: ChannelWrapper?) {
        PlayerManager.removeConnection(player)
        hookedJobs.removeIf { it.cancel();true }
        msgLock.withLock {
            disconnected = true
            messageReceiveQueue.removeIf { it.resume(null);true }
        }
    }

    fun registerContinuation(continuation: Continuation<String?>) {
        msgLock.withLock {
            if (disconnected) {
                continuation.resume(null)
            } else {
                messageReceiveQueue.add(continuation)
            }
        }
    }
}