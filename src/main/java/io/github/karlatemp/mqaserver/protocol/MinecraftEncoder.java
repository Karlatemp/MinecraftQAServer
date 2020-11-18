package io.github.karlatemp.mqaserver.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MinecraftEncoder extends MessageToByteEncoder<DefinedPacket> {

    private Protocol protocol;
    private boolean server;
    private int protocolVersion;

    public MinecraftEncoder(Protocol protocol, boolean server, int protocolVersion) {
        this.protocol = protocol;
        this.server = server;
        this.protocolVersion = protocolVersion;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, DefinedPacket msg, ByteBuf out) throws Exception {
        Protocol.DirectionData prot = (server) ? protocol.TO_CLIENT : protocol.TO_SERVER;
        DefinedPacket.writeVarInt(prot.getId(msg.getClass(), protocolVersion), out);
        msg.write(out, prot.getDirection(), protocolVersion);
    }
}
