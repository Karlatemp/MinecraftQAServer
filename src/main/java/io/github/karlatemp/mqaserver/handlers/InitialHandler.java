package io.github.karlatemp.mqaserver.handlers;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import io.github.karlatemp.mqaserver.core.IPlayer;
import io.github.karlatemp.mqaserver.core.IPlayerManager;
import io.github.karlatemp.mqaserver.core.ISystemCore;
import io.github.karlatemp.mqaserver.core.PlayerImpl;
import io.github.karlatemp.mqaserver.jni.cipher.BungeeCipher;
import io.github.karlatemp.mqaserver.netty.ChannelWrapper;
import io.github.karlatemp.mqaserver.netty.PacketHandler;
import io.github.karlatemp.mqaserver.netty.PipelineUtils;
import io.github.karlatemp.mqaserver.netty.cipher.CipherDecoder;
import io.github.karlatemp.mqaserver.netty.cipher.CipherEncoder;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.Protocol;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.github.karlatemp.mqaserver.protocol.PacketWrapper;
import io.github.karlatemp.mqaserver.protocol.packet.*;
import io.github.karlatemp.mqaserver.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class InitialHandler extends PacketHandler {

    private ChannelWrapper ch;
    private Handshake handshake;
    private LoginRequest loginRequest;
    private EncryptionRequest request;
    private final List<PluginMessage> relayMessages = new BoundedArrayList<>(128);
    private State thisState = State.HANDSHAKE;

    private boolean onlineMode = ISystemCore.getInstance().isOnlineMode();
    private InetSocketAddress virtualHost;
    private String name;
    private UUID uniqueId;
    private UUID offlineId;
    private LoginResult loginProfile;
    private boolean legacy;
    private String extraDataInHandshake = "";

    public InitialHandler() {
    }

    @Override
    public boolean shouldHandle(PacketWrapper packet) throws Exception {
        return !ch.isClosing();
    }


    public Handshake getHandshake() {
        return this.handshake;
    }

    public LoginRequest getLoginRequest() {
        return this.loginRequest;
    }

    public List<PluginMessage> getRelayMessages() {
        return this.relayMessages;
    }

    public boolean isOnlineMode() {
        return this.onlineMode;
    }

    public InetSocketAddress getVirtualHost() {
        return this.virtualHost;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public UUID getOfflineId() {
        return this.offlineId;
    }

    public LoginResult getLoginProfile() {
        return this.loginProfile;
    }

    public boolean isLegacy() {
        return this.legacy;
    }

    public String getExtraDataInHandshake() {
        return this.extraDataInHandshake;
    }

    private enum State {

        HANDSHAKE, STATUS, PING, USERNAME, ENCRYPT, FINISHED;
    }

    private boolean canSendKickMessage() {
        return thisState == State.USERNAME || thisState == State.ENCRYPT || thisState == State.FINISHED;
    }

    @Override
    public void connected(ChannelWrapper channel) throws Exception {
        this.ch = channel;
    }

    @Override
    public void exception(Throwable t) throws Exception {
        if (canSendKickMessage()) {
            disconnect(ChatColor.RED + Util.exception(t));
        } else {
            ch.close();
        }
    }

    @Override
    public void handle(PacketWrapper packet) throws Exception {
        if (packet.packet == null) {
            throw new QuietException("Unexpected packet received during login process! " + BufUtil.dump(packet.buf, 16));
        }
    }

    @Override
    public void handle(PluginMessage pluginMessage) throws Exception {
        // TODO: Unregister?
        if (PluginMessage.SHOULD_RELAY.apply(pluginMessage)) {
            relayMessages.add(pluginMessage);
        }
    }

    @Override
    public void handle(LegacyHandshake legacyHandshake) throws Exception {
        this.legacy = true;
        ch.close("Outdated client.");
    }

    @Override
    public void handle(LegacyPing ping) throws Exception {
        this.legacy = true;
        final boolean v1_5 = ping.isV1_5();

        ServerPing legacy = ISystemCore.getInstance().newServerPing(ISystemCore.getInstance().getProtocolVersion());


        String kickMessage;

        if (v1_5) {
            kickMessage = ChatColor.DARK_BLUE
                    + "\00" + 127
                    + '\00' + legacy.getVersion().getName()
                    + '\00' + getFirstLine(legacy.getDescription())
                    + '\00' + legacy.getPlayers().getOnline()
                    + '\00' + legacy.getPlayers().getMax();
        } else {
            // Clients <= 1.3 don't support colored motds because the color char is used as delimiter
            kickMessage = ChatColor.stripColor(getFirstLine(legacy.getDescription()))
                    + '\u00a7' + legacy.getPlayers().getOnline()
                    + '\u00a7' + legacy.getPlayers().getMax();
        }

        ch.close(kickMessage);
    }


    private static String getFirstLine(String str) {
        int pos = str.indexOf('\n');
        return pos == -1 ? str : str.substring(0, pos);
    }

    private ServerPing getPingInfo(int protocol) {
        return ISystemCore.getInstance().newServerPing(protocol);
    }

    @Override
    public void handle(StatusRequest statusRequest) throws Exception {
        Preconditions.checkState(thisState == State.STATUS, "Not expecting STATUS");

        final int protocol = (ProtocolConstants.SUPPORTED_VERSION_IDS.contains(handshake.getProtocolVersion())) ? handshake.getProtocolVersion() : ISystemCore.getInstance().getProtocolVersion();

        ServerPing info = getPingInfo(protocol);

        Gson gson = ISystemCore.getInstance().getGson();
        sendPacket(new StatusResponse(gson.toJson(info)));

        thisState = State.PING;
    }

    private void sendPacket(DefinedPacket packet) {
        ch.write(packet);
    }

    @Override
    public void handle(PingPacket ping) throws Exception {
        Preconditions.checkState(thisState == State.PING, "Not expecting PING");
        sendPacket(ping);
        disconnect("");
    }

    @Override
    public void handle(Handshake handshake) throws Exception {
        Preconditions.checkState(thisState == State.HANDSHAKE, "Not expecting HANDSHAKE");
        this.handshake = handshake;
        ch.setVersion(handshake.getProtocolVersion());

        // Starting with FML 1.8, a "\0FML\0" token is appended to the handshake. This interferes
        // with Bungee's IP forwarding, so we detect it, and remove it from the host string, for now.
        // We know FML appends \00FML\00. However, we need to also consider that other systems might
        // add their own data to the end of the string. So, we just take everything from the \0 character
        // and save it for later.
        if (handshake.getHost().contains("\0")) {
            String[] split = handshake.getHost().split("\0", 2);
            handshake.setHost(split[0]);
            extraDataInHandshake = "\0" + split[1];
        }

        // SRV records can end with a . depending on DNS / client.
        if (handshake.getHost().endsWith(".")) {
            handshake.setHost(handshake.getHost().substring(0, handshake.getHost().length() - 1));
        }

        this.virtualHost = InetSocketAddress.createUnresolved(handshake.getHost(), handshake.getPort());


        switch (handshake.getRequestedProtocol()) {
            case 1:
                // Ping
                thisState = State.STATUS;
                ch.setProtocol(Protocol.STATUS);
                break;
            case 2:
                // Login
                Log.getLogger().log(Level.INFO, "{0} has connected", this);
                thisState = State.USERNAME;
                ch.setProtocol(Protocol.LOGIN);

                if (!ProtocolConstants.SUPPORTED_VERSION_IDS.contains(handshake.getProtocolVersion())
                        || !ISystemCore.getInstance().isSupportedProtocol(handshake.getProtocolVersion())
                ) {
                    if (handshake.getProtocolVersion() > ISystemCore.getInstance().getProtocolVersion()) {
                        disconnect("Outdated server");
                    } else {
                        disconnect("Outdated client");
                    }
                    return;
                }
                break;
            default:
                throw new QuietException("Cannot request protocol " + handshake.getRequestedProtocol());
        }
    }

    @Override
    public void handle(LoginRequest loginRequest) throws Exception {
        Preconditions.checkState(thisState == State.USERNAME, "Not expecting USERNAME");
        this.loginRequest = loginRequest;

        if (getName().contains(".")) {
            disconnect("Invalid name");
            return;
        }

        if (getName().length() > 16) {
            disconnect("Name tag too long");
            return;
        }

//        int limit = BungeeCord.getInstance().config.getPlayerLimit();
//        if (limit > 0 && bungee.getOnlineCount() > limit) {
//            disconnect(bungee.getTranslation("proxy_full"));
//            return;
//        }

        // If offline mode and they are already on, don't allow connect
        // We can just check by UUID here as names are based on UUID
        // TODO
//        if (!isOnlineMode() && bungee.getPlayer(getUniqueId()) != null) {
//            disconnect(bungee.getTranslation("already_connected_proxy"));
//            return;
//        }

        if (ch.isClosed()) {
            return;
        }
        if (onlineMode) {
            sendPacket(request = EncryptionUtil.encryptRequest());
        } else {
            finish();
        }
        thisState = State.ENCRYPT;
    }

    @Override
    public void handle(final EncryptionResponse encryptResponse) throws Exception {
        Preconditions.checkState(thisState == State.ENCRYPT, "Not expecting ENCRYPT");

        SecretKey sharedKey = EncryptionUtil.getSecret(encryptResponse, request);
        BungeeCipher decrypt = EncryptionUtil.getCipher(false, sharedKey);
        ch.addBefore(PipelineUtils.FRAME_DECODER, PipelineUtils.DECRYPT_HANDLER, new CipherDecoder(decrypt));
        BungeeCipher encrypt = EncryptionUtil.getCipher(true, sharedKey);
        ch.addBefore(PipelineUtils.FRAME_PREPENDER, PipelineUtils.ENCRYPT_HANDLER, new CipherEncoder(encrypt));

        String encName = URLEncoder.encode(InitialHandler.this.getName(), "UTF-8");

        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        for (byte[] bit : new byte[][]
                {
                        request.getServerId().getBytes("ISO_8859_1"), sharedKey.getEncoded(), EncryptionUtil.keys.getPublic().getEncoded()
                }) {
            sha.update(bit);
        }
        String encodedHash = URLEncoder.encode(new BigInteger(sha.digest()).toString(16), "UTF-8");

        String preventProxy = (ISystemCore.getInstance().isPreventProxyConnections() && getSocketAddress() instanceof InetSocketAddress) ? "&ip=" + URLEncoder.encode(getAddress().getAddress().getHostAddress(), "UTF-8") : "";
        String authURL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + encName + "&serverId=" + encodedHash + preventProxy;

        ISystemCore.getInstance().httpGet(authURL, (result, error) -> {
            if (error == null) {
                LoginResult obj = ISystemCore.getInstance().getGson().fromJson(result, LoginResult.class);
                if (obj != null && obj.getId() != null) {
                    loginProfile = obj;
                    name = obj.getName();
                    uniqueId = Util.getUUID(obj.getId());
                    finish();
                    return;
                }
                disconnect("Offline mode player");
            } else {
                disconnect("Mojang auth server down.");
                Log.getLogger().log(Level.SEVERE, "Error authenticating " + getName() + " with minecraft.net", error);
            }
        });
    }

    private void finish() {
        if (isOnlineMode()) {
            // Check for multiple connections
            // We have to check for the old name first
            IPlayer oldName = IPlayerManager.instance().getPlayer(getName());
            if (oldName != null) {
                // TODO See SpigotMc/BungeeCord#1218
                oldName.disconnect("already_connected_proxy");
            }
            // And then also for their old UUID
            IPlayer oldID = IPlayerManager.instance().getPlayer(getUniqueId());
            if (oldID != null) {
                // TODO See SpigotMc/BungeeCord#1218
                oldID.disconnect("already_connected_proxy");
            }
        } else {
            // In offline mode the existing user stays and we kick the new one
            IPlayer oldName = IPlayerManager.instance().getPlayer(getName());
            if (oldName != null) {
                // TODO See SpigotMc/BungeeCord#1218
                disconnect("already_connected_proxy");
                return;
            }

        }

        offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + getName()).getBytes(Charsets.UTF_8));
        if (uniqueId == null) {
            uniqueId = offlineId;
        }

        if (ch.isClosed()) {
            return;
        }

        ch.getHandle().eventLoop().execute(() -> {
            if (!ch.isClosing()) {

                PlayerImpl userCon = new PlayerImpl(ch, getName(), this);

                sendPacket(new LoginSuccess(getUniqueId(), getName()));
                ch.setProtocol(Protocol.GAME);

                UpstreamBridge bridge = ISystemCore.getInstance().newUpstreamBridge(userCon);
                ch.getHandle().pipeline().get(HandlerBoss.class)
                        .setHandler(bridge);
                bridge.setup();
                thisState = State.FINISHED;
            }
        });
    }

    @Override
    public void handle(LoginPayloadResponse response) throws Exception {
        disconnect("Unexpected custom LoginPayloadResponse");
    }

    public void disconnect(String reason) {
        if (canSendKickMessage()) {
            disconnect(TextComponent.fromLegacyText(reason));
        } else {
            ch.close();
        }
    }

    public void disconnect(final BaseComponent... reason) {
        if (canSendKickMessage()) {
            ch.delayedClose(new Kick(ComponentSerializer.toString(reason)));
        } else {
            ch.close();
        }
    }

    public void disconnect(BaseComponent reason) {
        disconnect(new BaseComponent[]
                {
                        reason
                });
    }

    public String getName() {
        return (name != null) ? name : (loginRequest == null) ? null : loginRequest.getData();
    }

    public int getVersion() {
        return (handshake == null) ? -1 : handshake.getProtocolVersion();
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) getSocketAddress();
    }

    public SocketAddress getSocketAddress() {
        return ch.getRemoteAddress();
    }


    public void setOnlineMode(boolean onlineMode) {
        Preconditions.checkState(thisState == State.USERNAME, "Can only set online mode status whilst state is username");
        this.onlineMode = onlineMode;
    }

    public void setUniqueId(UUID uuid) {
        Preconditions.checkState(thisState == State.USERNAME, "Can only set uuid while state is username");
        Preconditions.checkState(!onlineMode, "Can only set uuid when online mode is false");
        this.uniqueId = uuid;
    }

    public String getUUID() {
        return uniqueId.toString().replace("-", "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        String currentName = getName();
        if (currentName != null) {
            sb.append(currentName);
            sb.append(',');
        }

        sb.append(getSocketAddress());
        sb.append("] <-> InitialHandler");

        return sb.toString();
    }

    public boolean isConnected() {
        return !ch.isClosed();
    }
}
