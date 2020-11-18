package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class EncryptionRequest extends DefinedPacket {

    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    public EncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public EncryptionRequest() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        serverId = readString(buf);
        publicKey = readArray(buf);
        verifyToken = readArray(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(serverId, buf);
        writeArray(publicKey, buf);
        writeArray(verifyToken, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getServerId() {
        return this.serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public byte[] getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public void setVerifyToken(byte[] verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String toString() {
        return "EncryptionRequest(serverId=" + this.serverId + ", publicKey=" + java.util.Arrays.toString(this.publicKey) + ", verifyToken=" + java.util.Arrays.toString(this.verifyToken) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EncryptionRequest)) return false;
        final EncryptionRequest other = (EncryptionRequest) o;
        if (!other.canEqual(this)) return false;
        final Object this$serverId = this.getServerId();
        final Object other$serverId = other.getServerId();
        if (this$serverId == null ? other$serverId != null : !this$serverId.equals(other$serverId)) return false;
        if (!java.util.Arrays.equals(this.getPublicKey(), other.getPublicKey())) return false;
        return java.util.Arrays.equals(this.getVerifyToken(), other.getVerifyToken());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EncryptionRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serverId = this.getServerId();
        result = result * PRIME + ($serverId == null ? 43 : $serverId.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getPublicKey());
        result = result * PRIME + java.util.Arrays.hashCode(this.getVerifyToken());
        return result;
    }
}
