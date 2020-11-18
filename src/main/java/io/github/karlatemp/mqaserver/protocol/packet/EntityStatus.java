package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class EntityStatus extends DefinedPacket {

    public static final byte DEBUG_INFO_REDUCED = 22;
    public static final byte DEBUG_INFO_NORMAL = 23;
    //
    private int entityId;
    private byte status;

    public EntityStatus(int entityId, byte status) {
        this.entityId = entityId;
        this.status = status;
    }

    public EntityStatus() {
    }

    @Override
    public void read(ByteBuf buf) {
        entityId = buf.readInt();
        status = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(status);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String toString() {
        return "EntityStatus(entityId=" + this.entityId + ", status=" + this.status + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EntityStatus)) return false;
        final EntityStatus other = (EntityStatus) o;
        if (!other.canEqual(this)) return false;
        if (this.getEntityId() != other.getEntityId()) return false;
        return this.getStatus() == other.getStatus();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EntityStatus;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getEntityId();
        result = result * PRIME + this.getStatus();
        return result;
    }
}
