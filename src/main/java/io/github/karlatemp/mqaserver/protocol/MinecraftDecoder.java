package io.github.karlatemp.mqaserver.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class MinecraftDecoder extends MessageToMessageDecoder<ByteBuf> {

    private Protocol protocol;
    private final boolean server;
    private int protocolVersion;

    public MinecraftDecoder(Protocol protocol, boolean server, int protocolVersion) {
        this.protocol = protocol;
        this.server = server;
        this.protocolVersion = protocolVersion;
    }


    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Protocol.DirectionData prot = (server) ? protocol.TO_SERVER : protocol.TO_CLIENT;
        ByteBuf slice = in.copy(); // Can't slice this one due to EntityMap :(

        try {
            int packetId = DefinedPacket.readVarInt(in);

            DefinedPacket packet = prot.createPacket(packetId, protocolVersion);
            if (packet != null) {
                packet.read(in, prot.getDirection(), protocolVersion);

                if (in.isReadable()) {
                    throw new BadPacketException("Did not read all bytes from packet " + packet.getClass() + " " + packetId + " Protocol " + protocol + " Direction " + prot.getDirection());
                }
            } else {
                in.skipBytes(in.readableBytes());
            }

            out.add(new PacketWrapper(packet, slice));
            slice = null;
        } finally {
            if (slice != null) {
                slice.release();
            }
        }
    }

    public int getProtocolVersion() {
        return protocolVersion;
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

}
