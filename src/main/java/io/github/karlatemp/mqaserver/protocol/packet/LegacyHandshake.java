package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class LegacyHandshake extends DefinedPacket {

    public LegacyHandshake() {
    }

    @Override
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String toString() {
        return "LegacyHandshake()";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LegacyHandshake)) return false;
        final LegacyHandshake other = (LegacyHandshake) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LegacyHandshake;
    }

    public int hashCode() {
        int result = 1;
        return result;
    }
}
