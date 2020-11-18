package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class KeepAlive extends DefinedPacket {

    private long randomId;

    public KeepAlive(long randomId) {
        this.randomId = randomId;
    }

    public KeepAlive() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        randomId = (protocolVersion >= ProtocolConstants.MINECRAFT_1_12_2) ? buf.readLong() : readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_12_2) {
            buf.writeLong(randomId);
        } else {
            writeVarInt((int) randomId, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public long getRandomId() {
        return this.randomId;
    }

    public void setRandomId(long randomId) {
        this.randomId = randomId;
    }

    public String toString() {
        return "KeepAlive(randomId=" + this.randomId + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof KeepAlive)) return false;
        final KeepAlive other = (KeepAlive) o;
        if (!other.canEqual(this)) return false;
        return this.getRandomId() == other.getRandomId();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof KeepAlive;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $randomId = this.getRandomId();
        result = result * PRIME + (int) ($randomId >>> 32 ^ $randomId);
        return result;
    }
}
