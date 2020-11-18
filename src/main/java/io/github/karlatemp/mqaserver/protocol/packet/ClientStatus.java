package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class ClientStatus extends DefinedPacket {

    private byte payload;

    public ClientStatus(byte payload) {
        this.payload = payload;
    }

    public ClientStatus() {
    }

    @Override
    public void read(ByteBuf buf) {
        payload = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(payload);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public byte getPayload() {
        return this.payload;
    }

    public void setPayload(byte payload) {
        this.payload = payload;
    }

    public String toString() {
        return "ClientStatus(payload=" + this.payload + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClientStatus)) return false;
        final ClientStatus other = (ClientStatus) o;
        if (!other.canEqual(this)) return false;
        return this.getPayload() == other.getPayload();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ClientStatus;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getPayload();
        return result;
    }
}
