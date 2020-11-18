package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class GameState extends DefinedPacket {

    public static final short IMMEDIATE_RESPAWN = 11;
    //
    private short state;
    private float value;

    public GameState(short state, float value) {
        this.state = state;
        this.value = value;
    }

    public GameState() {
    }

    @Override
    public void read(ByteBuf buf) {
        state = buf.readUnsignedByte();
        value = buf.readFloat();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(state);
        buf.writeFloat(value);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public short getState() {
        return this.state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String toString() {
        return "GameState(state=" + this.state + ", value=" + this.value + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GameState)) return false;
        final GameState other = (GameState) o;
        if (!other.canEqual(this)) return false;
        if (this.getState() != other.getState()) return false;
        return Float.compare(this.getValue(), other.getValue()) == 0;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GameState;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getState();
        result = result * PRIME + Float.floatToIntBits(this.getValue());
        return result;
    }
}
