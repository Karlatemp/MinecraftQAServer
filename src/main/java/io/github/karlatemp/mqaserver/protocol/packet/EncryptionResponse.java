package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class EncryptionResponse extends DefinedPacket {

    private byte[] sharedSecret;
    private byte[] verifyToken;

    public EncryptionResponse(byte[] sharedSecret, byte[] verifyToken) {
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
    }

    public EncryptionResponse() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        sharedSecret = readArray(buf, 128);
        verifyToken = readArray(buf, 128);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeArray(sharedSecret, buf);
        writeArray(verifyToken, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    public void setSharedSecret(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public void setVerifyToken(byte[] verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String toString() {
        return "EncryptionResponse(sharedSecret=" + java.util.Arrays.toString(this.sharedSecret) + ", verifyToken=" + java.util.Arrays.toString(this.verifyToken) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EncryptionResponse)) return false;
        final EncryptionResponse other = (EncryptionResponse) o;
        if (!other.canEqual(this)) return false;
        if (!java.util.Arrays.equals(this.getSharedSecret(), other.getSharedSecret())) return false;
        return java.util.Arrays.equals(this.getVerifyToken(), other.getVerifyToken());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EncryptionResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.hashCode(this.getSharedSecret());
        result = result * PRIME + java.util.Arrays.hashCode(this.getVerifyToken());
        return result;
    }
}
