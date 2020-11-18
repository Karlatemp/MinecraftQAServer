package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class ScoreboardDisplay extends DefinedPacket {

    /**
     * 0 = list, 1 = side, 2 = below.
     */
    private byte position;
    private String name;

    public ScoreboardDisplay(byte position, String name) {
        this.position = position;
        this.name = name;
    }

    public ScoreboardDisplay() {
    }

    @Override
    public void read(ByteBuf buf) {
        position = buf.readByte();
        name = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(position);
        writeString(name, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public byte getPosition() {
        return this.position;
    }

    public void setPosition(byte position) {
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "ScoreboardDisplay(position=" + this.position + ", name=" + this.name + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ScoreboardDisplay)) return false;
        final ScoreboardDisplay other = (ScoreboardDisplay) o;
        if (!other.canEqual(this)) return false;
        if (this.getPosition() != other.getPosition()) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        return this$name == null ? other$name == null : this$name.equals(other$name);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ScoreboardDisplay;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getPosition();
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }
}
