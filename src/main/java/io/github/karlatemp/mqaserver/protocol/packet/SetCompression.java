package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class SetCompression extends DefinedPacket {

    private int threshold;

    public SetCompression(int threshold) {
        this.threshold = threshold;
    }

    public SetCompression() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        threshold = DefinedPacket.readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        DefinedPacket.writeVarInt(threshold, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String toString() {
        return "SetCompression(threshold=" + this.threshold + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SetCompression)) return false;
        final SetCompression other = (SetCompression) o;
        if (!other.canEqual(this)) return false;
        return this.getThreshold() == other.getThreshold();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SetCompression;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getThreshold();
        return result;
    }
}
