package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class BossBar extends DefinedPacket {

    private UUID uuid;
    private int action;
    private String title;
    private float health;
    private int color;
    private int division;
    private byte flags;

    public BossBar(UUID uuid, int action) {
        this.uuid = uuid;
        this.action = action;
    }

    public BossBar() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        uuid = readUUID(buf);
        action = readVarInt(buf);

        switch (action) {
            // Add
            case 0:
                title = readString(buf);
                health = buf.readFloat();
                color = readVarInt(buf);
                division = readVarInt(buf);
                flags = buf.readByte();
                break;
            // Health
            case 2:
                health = buf.readFloat();
                break;
            // Title
            case 3:
                title = readString(buf);
                break;
            // Style
            case 4:
                color = readVarInt(buf);
                division = readVarInt(buf);
                break;
            // Flags
            case 5:
                flags = buf.readByte();
                break;
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeUUID(uuid, buf);
        writeVarInt(action, buf);

        switch (action) {
            // Add
            case 0:
                writeString(title, buf);
                buf.writeFloat(health);
                writeVarInt(color, buf);
                writeVarInt(division, buf);
                buf.writeByte(flags);
                break;
            // Health
            case 2:
                buf.writeFloat(health);
                break;
            // Title
            case 3:
                writeString(title, buf);
                break;
            // Style
            case 4:
                writeVarInt(color, buf);
                writeVarInt(division, buf);
                break;
            // Flags
            case 5:
                buf.writeByte(flags);
                break;
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDivision() {
        return this.division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public byte getFlags() {
        return this.flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public String toString() {
        return "BossBar(uuid=" + this.uuid + ", action=" + this.action + ", title=" + this.title + ", health=" + this.health + ", color=" + this.color + ", division=" + this.division + ", flags=" + this.flags + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BossBar)) return false;
        final BossBar other = (BossBar) o;
        if (!other.canEqual(this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        if (this.getAction() != other.getAction()) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        if (Float.compare(this.getHealth(), other.getHealth()) != 0) return false;
        if (this.getColor() != other.getColor()) return false;
        if (this.getDivision() != other.getDivision()) return false;
        return this.getFlags() == other.getFlags();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BossBar;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        result = result * PRIME + this.getAction();
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        result = result * PRIME + Float.floatToIntBits(this.getHealth());
        result = result * PRIME + this.getColor();
        result = result * PRIME + this.getDivision();
        result = result * PRIME + this.getFlags();
        return result;
    }
}
