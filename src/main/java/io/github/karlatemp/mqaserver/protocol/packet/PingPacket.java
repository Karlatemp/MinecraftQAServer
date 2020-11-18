package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class PingPacket extends DefinedPacket {

    private long time;

    public PingPacket(long time) {
        this.time = time;
    }

    public PingPacket() {
    }

    @Override
    public void read(ByteBuf buf) {
        time = buf.readLong();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(time);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String toString() {
        return "PingPacket(time=" + this.time + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PingPacket)) return false;
        final PingPacket other = (PingPacket) o;
        if (!other.canEqual(this)) return false;
        return this.getTime() == other.getTime();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PingPacket;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $time = this.getTime();
        result = result * PRIME + (int) ($time >>> 32 ^ $time);
        return result;
    }
}
