package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class LegacyPing extends DefinedPacket {

    private final boolean v1_5;

    public LegacyPing(boolean v1_5) {
        this.v1_5 = v1_5;
    }

    @Override
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write(ByteBuf buf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public boolean isV1_5() {
        return this.v1_5;
    }

    public String toString() {
        return "LegacyPing(v1_5=" + this.v1_5 + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LegacyPing)) return false;
        final LegacyPing other = (LegacyPing) o;
        if (!other.canEqual(this)) return false;
        return this.isV1_5() == other.isV1_5();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LegacyPing;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isV1_5() ? 79 : 97);
        return result;
    }
}
