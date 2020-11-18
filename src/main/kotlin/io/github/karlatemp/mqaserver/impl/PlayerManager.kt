package io.github.karlatemp.mqaserver.impl

import io.github.karlatemp.mqaserver.core.IPlayer
import io.github.karlatemp.mqaserver.core.IPlayerManager
import io.github.karlatemp.mqaserver.core.PlayerImpl
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.collections.HashMap
import kotlin.concurrent.withLock

object PlayerManager : IPlayerManager {
    internal val connectionLock = ReentrantReadWriteLock()
    internal val connectionByName = HashMap<String, PlayerImpl>()
    internal val connectionByUid = HashMap<UUID, PlayerImpl>()

    override fun getPlayer(name: String): IPlayer? = connectionLock.readLock().withLock {
        connectionByName[name.toLowerCase()]
    }

    override fun getPlayer(uuid: UUID): IPlayer? = connectionLock.readLock().withLock {
        connectionByUid[uuid]
    }

    override fun putConnection(player: IPlayer) {
        connectionLock.writeLock().withLock {
            connectionByName[player.name.toLowerCase()] = player as PlayerImpl
            connectionByUid[player.uniqueId] = player
        }
    }

    override fun removeConnection(player: IPlayer) {
        connectionLock.writeLock().withLock {
            if (connectionByName[player.name.toLowerCase()] === player) {
                connectionByName.remove(player.name.toLowerCase())
                connectionByUid.remove(player.uniqueId)
            }
        }
    }

}