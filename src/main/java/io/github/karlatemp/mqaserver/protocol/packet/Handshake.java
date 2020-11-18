package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class Handshake extends DefinedPacket {

    private int protocolVersion;
    private String host;
    private int port;
    private int requestedProtocol;

    public Handshake(int protocolVersion, String host, int port, int requestedProtocol) {
        this.protocolVersion = protocolVersion;
        this.host = host;
        this.port = port;
        this.requestedProtocol = requestedProtocol;
    }

    public Handshake() {
    }

    @Override
    public void read(ByteBuf buf) {
        protocolVersion = readVarInt(buf);
        host = readString(buf);
        port = buf.readUnsignedShort();
        requestedProtocol = readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(protocolVersion, buf);
        writeString(host, buf);
        buf.writeShort(port);
        writeVarInt(requestedProtocol, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestedProtocol() {
        return this.requestedProtocol;
    }

    public void setRequestedProtocol(int requestedProtocol) {
        this.requestedProtocol = requestedProtocol;
    }

    public String toString() {
        return "Handshake(protocolVersion=" + this.protocolVersion + ", host=" + this.host + ", port=" + this.port + ", requestedProtocol=" + this.requestedProtocol + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Handshake)) return false;
        final Handshake other = (Handshake) o;
        if (!other.canEqual(this)) return false;
        if (this.getProtocolVersion() != other.getProtocolVersion()) return false;
        final Object this$host = this.getHost();
        final Object other$host = other.getHost();
        if (this$host == null ? other$host != null : !this$host.equals(other$host)) return false;
        if (this.getPort() != other.getPort()) return false;
        return this.getRequestedProtocol() == other.getRequestedProtocol();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Handshake;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getProtocolVersion();
        final Object $host = this.getHost();
        result = result * PRIME + ($host == null ? 43 : $host.hashCode());
        result = result * PRIME + this.getPort();
        result = result * PRIME + this.getRequestedProtocol();
        return result;
    }
}
