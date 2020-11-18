package io.github.karlatemp.mqaserver

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.karlatemp.mqaserver.core.IPlayerManager
import io.github.karlatemp.mqaserver.core.ISystemCore
import io.github.karlatemp.mqaserver.core.ISystemCore.Companion.startServer
import io.github.karlatemp.mqaserver.core.PlayerImpl
import io.github.karlatemp.mqaserver.handlers.UpstreamBridge
import io.github.karlatemp.mqaserver.impl.PlayerManager
import io.github.karlatemp.mqaserver.impl.UBImpl
import io.github.karlatemp.mqaserver.netty.PipelineUtils
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants
import io.github.karlatemp.mqaserver.protocol.packet.JoinGame
import io.github.karlatemp.mqaserver.protocol.packet.KeepAlive
import io.github.karlatemp.mqaserver.utils.*
import io.netty.channel.EventLoopGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import net.md_5.bungee.api.chat.*
import net.md_5.bungee.chat.*
import okhttp3.*
import java.io.IOException
import java.net.SocketAddress
import java.security.SecureRandom
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

object Bootstrap : ISystemCore {
    var application: Application? = null
    val scope = CoroutineScope(Dispatchers.Default)
    val startup by lazy<Unit> {
        Log.setLogger(Logger.getLogger("Minecraft QA Server"))
        ISystemCore.Companion.setInstance(this)
        startServer()
        thread(
            name = "Keep alive poster",
            isDaemon = true
        ) {
            val ram = SecureRandom()
            while (true) {
                Thread.sleep(2000)
                PlayerManager.run {
                    connectionLock.readLock().withLock {
                        connectionByUid.values.forEach { player ->
                            kotlin.runCatching {
                                player.sendPacket(KeepAlive(ram.nextLong()))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getPlayerManager0(): IPlayerManager = PlayerManager
    override fun isSupportedProtocol(protocolVersion: Int): Boolean {
        return true
    }

    override fun getProtocolVersion(): Int = ProtocolConstants.MINECRAFT_1_16_1

    public val _eventLoopGroup = PipelineUtils.newEventLoopGroup(3, object : ThreadFactory {
        val counter = AtomicLong()
        override fun newThread(r: Runnable): Thread =
            Thread(r, "Minecraft QA Server Thread#${counter.getAndIncrement()}")
    })

    override fun getEventLoopGroup(): EventLoopGroup = _eventLoopGroup

    @get:JvmName("isOnlineMode0")
    public var onlineMode: Boolean = false
    override fun isOnlineMode(): Boolean = onlineMode

    var serverPing: (protocolVersion: Int) -> ServerPing = { protocolVersion ->
        ServerPing(
            ServerPing.Protocol("Minecraft QA Server", protocolVersion),
            ServerPing.Players(1919810, 114514, arrayOf()),
            TextComponent.fromLegacyText("§6Powered by Karlatemp").component,
            null
        )
    }

    override fun newServerPing(protocolVersion: Int): ServerPing = serverPing(protocolVersion)

    private val _gson = GsonBuilder()
        .registerTypeAdapter(BaseComponent::class.java, ComponentSerializer())
        .registerTypeAdapter(TextComponent::class.java, TextComponentSerializer())
        .registerTypeAdapter(TranslatableComponent::class.java, TranslatableComponentSerializer())
        .registerTypeAdapter(KeybindComponent::class.java, KeybindComponentSerializer())
        .registerTypeAdapter(ScoreComponent::class.java, ScoreComponentSerializer())
        .registerTypeAdapter(SelectorComponent::class.java, SelectorComponentSerializer())
        .registerTypeAdapter(ServerPing.PlayerInfo::class.java, PlayerInfoSerializer())
        .registerTypeAdapter(Favicon::class.java, Favicon.getFaviconTypeAdapter()).create()

    override fun getGson(): Gson = _gson

    override fun isPreventProxyConnections(): Boolean = false

    public var _addr = Util.getAddr("0.0.0.0:25565")
    override fun getSocketAddress(): SocketAddress = _addr

    private val httpClient = OkHttpClient()
    override fun httpGet(url: String, callback: ResultCallback<String>) {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.done(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback.done(null, IOException("Unexpected code $response"))
                } else {
                    callback.done(response.body!!.string(), null)
                }
            }
        })
    }

    override fun newUpstreamBridge(
        player: PlayerImpl
    ): UpstreamBridge = UBImpl(player)

    public var defaultLocation = doubleArrayOf(.0, .0, .0)
    override fun newJoinLocation(): DoubleArray? = defaultLocation

    public var worldType: JoinGame.WorldType = JoinGame.WorldType.WORLD_THE_END
    override fun worldType(): JoinGame.WorldType = worldType
}
