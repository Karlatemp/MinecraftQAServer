package io.github.karlatemp.mqaserver

import io.github.karlatemp.mqaserver.core.IPlayer
import io.github.karlatemp.mqaserver.core.PlayerImpl
import io.github.karlatemp.mqaserver.impl.UBImpl
import kotlin.coroutines.suspendCoroutine

private val IPlayer.impl: UBImpl get() = (this as PlayerImpl).extData as UBImpl

open class Application {
    suspend inline fun IPlayer.ask(block: IPlayer.() -> Unit): String {
        block(); return receiveMessage()
    }

    suspend inline fun IPlayer.askOrNull(block: IPlayer.() -> Unit): String? {
        block(); return receiveMessageOrNull()
    }

    suspend fun IPlayer.receiveMessage(): String {
        return receiveMessageOrNull() ?: error("Player quited")
    }

    suspend fun IPlayer.receiveMessageOrNull(): String? {
        val impl = this.impl
        val ip = this as PlayerImpl
        if (ip.ch.let { it.isClosed || it.isClosing }) return null
        return suspendCoroutine { impl.registerContinuation(it) }
    }

    open suspend fun onPlayerConnect(player: IPlayer) {}

    fun IPlayer.updateServerName(name: String) {
        impl.updateServerName(name)
    }

    var IPlayer.onMessage: MessageHandler?
        get() = impl.onMessage
        set(value) {
            impl.onMessage = value
        }
    var IPlayer.onMessageNotReceived: MessageHandler?
        get() = impl.onMessageNotReceived
        set(value) {
            impl.onMessageNotReceived = value
        }

}

class StandardApplication internal constructor(
    val onPlayerConnect: suspend Application.(IPlayer) -> Unit
) : Application() {
    override suspend fun onPlayerConnect(player: IPlayer) {
        onPlayerConnect.invoke(this, player)
    }
}
typealias MessageHandler = (String) -> Unit

fun setupApplication(
    onPlayerConnect: suspend Application.(IPlayer) -> Unit
) {
    Bootstrap.application = StandardApplication(onPlayerConnect)
    Bootstrap.startup
}
