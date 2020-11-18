package io.github.karlatemp.mqaserver.protocol;

import io.github.karlatemp.mqaserver.protocol.packet.LegacyHandshake;
import io.github.karlatemp.mqaserver.protocol.packet.LegacyPing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class LegacyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable()) {
            return;
        }

        in.markReaderIndex();
        short packetID = in.readUnsignedByte();

        if (packetID == 0xFE) {
            out.add(new PacketWrapper(new LegacyPing(in.isReadable() && in.readUnsignedByte() == 0x01), Unpooled.EMPTY_BUFFER));
            return;
        } else if (packetID == 0x02 && in.isReadable()) {
            in.skipBytes(in.readableBytes());
            out.add(new PacketWrapper(new LegacyHandshake(), Unpooled.EMPTY_BUFFER));
            return;
        }

        in.resetReaderIndex();
        ctx.pipeline().remove(this);
    }
}
