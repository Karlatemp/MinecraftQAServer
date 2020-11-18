package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class StatusRequest extends DefinedPacket {

    public StatusRequest() {
    }

    @Override
    public void read(ByteBuf buf) {
    }

    @Override
    public void write(ByteBuf buf) {
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String toString() {
        return "StatusRequest()";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof StatusRequest)) return false;
        final StatusRequest other = (StatusRequest) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof StatusRequest;
    }

    public int hashCode() {
        int result = 1;
        return result;
    }
}
