package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class ViewDistance extends DefinedPacket {

    private int distance;

    public ViewDistance(int distance) {
        this.distance = distance;
    }

    public ViewDistance() {
    }

    @Override
    public void read(ByteBuf buf) {
        distance = DefinedPacket.readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        DefinedPacket.writeVarInt(distance, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String toString() {
        return "ViewDistance(distance=" + this.distance + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ViewDistance)) return false;
        final ViewDistance other = (ViewDistance) o;
        if (!other.canEqual(this)) return false;
        return this.getDistance() == other.getDistance();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ViewDistance;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getDistance();
        return result;
    }
}
