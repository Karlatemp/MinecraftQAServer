package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.util.Locale;

public class ScoreboardObjective extends DefinedPacket {

    private String name;
    private String value;
    private HealthDisplay type;
    /**
     * 0 to create, 1 to remove, 2 to update display text.
     */
    private byte action;

    public ScoreboardObjective(String name, String value, HealthDisplay type, byte action) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.action = action;
    }

    public ScoreboardObjective() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        name = readString(buf);
        action = buf.readByte();
        if (action == 0 || action == 2) {
            value = readString(buf);
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
                type = HealthDisplay.values()[readVarInt(buf)];
            } else {
                type = HealthDisplay.fromString(readString(buf));
            }
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(name, buf);
        buf.writeByte(action);
        if (action == 0 || action == 2) {
            writeString(value, buf);
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
                writeVarInt(type.ordinal(), buf);
            } else {
                writeString(type.toString(), buf);
            }
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HealthDisplay getType() {
        return this.type;
    }

    public void setType(HealthDisplay type) {
        this.type = type;
    }

    public byte getAction() {
        return this.action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public String toString() {
        return "ScoreboardObjective(name=" + this.name + ", value=" + this.value + ", type=" + this.type + ", action=" + this.action + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ScoreboardObjective)) return false;
        final ScoreboardObjective other = (ScoreboardObjective) o;
        if (!other.canEqual(this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        return this.getAction() == other.getAction();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ScoreboardObjective;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + this.getAction();
        return result;
    }

    public enum HealthDisplay {

        INTEGER, HEARTS;

        public static HealthDisplay fromString(String s) {
            return valueOf(s.toUpperCase(Locale.ROOT));
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }
    }
}
