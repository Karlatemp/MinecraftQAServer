package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class Team extends DefinedPacket {

    private String name;
    /**
     * 0 - create, 1 remove, 2 info update, 3 player add, 4 player remove.
     */
    private byte mode;
    private String displayName;
    private String prefix;
    private String suffix;
    private String nameTagVisibility;
    private String collisionRule;
    private int color;
    private byte friendlyFire;
    private String[] players;

    /**
     * Packet to destroy a team.
     *
     * @param name team name
     */
    public Team(String name) {
        this.name = name;
        this.mode = 1;
    }

    public Team(String name, byte mode, String displayName, String prefix, String suffix, String nameTagVisibility, String collisionRule, int color, byte friendlyFire, String[] players) {
        this.name = name;
        this.mode = mode;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.nameTagVisibility = nameTagVisibility;
        this.collisionRule = collisionRule;
        this.color = color;
        this.friendlyFire = friendlyFire;
        this.players = players;
    }

    public Team() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        name = readString(buf);
        mode = buf.readByte();
        if (mode == 0 || mode == 2) {
            displayName = readString(buf);
            if (protocolVersion < ProtocolConstants.MINECRAFT_1_13) {
                prefix = readString(buf);
                suffix = readString(buf);
            }
            friendlyFire = buf.readByte();
            nameTagVisibility = readString(buf);
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
                collisionRule = readString(buf);
            }
            color = (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) ? readVarInt(buf) : buf.readByte();
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
                prefix = readString(buf);
                suffix = readString(buf);
            }
        }
        if (mode == 0 || mode == 3 || mode == 4) {
            int len = readVarInt(buf);
            players = new String[len];
            for (int i = 0; i < len; i++) {
                players[i] = readString(buf);
            }
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(name, buf);
        buf.writeByte(mode);
        if (mode == 0 || mode == 2) {
            writeString(displayName, buf);
            if (protocolVersion < ProtocolConstants.MINECRAFT_1_13) {
                writeString(prefix, buf);
                writeString(suffix, buf);
            }
            buf.writeByte(friendlyFire);
            writeString(nameTagVisibility, buf);
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
                writeString(collisionRule, buf);
            }

            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
                writeVarInt(color, buf);
                writeString(prefix, buf);
                writeString(suffix, buf);
            } else {
                buf.writeByte(color);
            }
        }
        if (mode == 0 || mode == 3 || mode == 4) {
            writeVarInt(players.length, buf);
            for (String player : players) {
                writeString(player, buf);
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

    public byte getMode() {
        return this.mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    public void setNameTagVisibility(String nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
    }

    public String getCollisionRule() {
        return this.collisionRule;
    }

    public void setCollisionRule(String collisionRule) {
        this.collisionRule = collisionRule;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public byte getFriendlyFire() {
        return this.friendlyFire;
    }

    public void setFriendlyFire(byte friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public String[] getPlayers() {
        return this.players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public String toString() {
        return "Team(name=" + this.name + ", mode=" + this.mode + ", displayName=" + this.displayName + ", prefix=" + this.prefix + ", suffix=" + this.suffix + ", nameTagVisibility=" + this.nameTagVisibility + ", collisionRule=" + this.collisionRule + ", color=" + this.color + ", friendlyFire=" + this.friendlyFire + ", players=" + java.util.Arrays.deepToString(this.players) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Team)) return false;
        final Team other = (Team) o;
        if (!other.canEqual(this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.getMode() != other.getMode()) return false;
        final Object this$displayName = this.getDisplayName();
        final Object other$displayName = other.getDisplayName();
        if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName))
            return false;
        final Object this$prefix = this.getPrefix();
        final Object other$prefix = other.getPrefix();
        if (this$prefix == null ? other$prefix != null : !this$prefix.equals(other$prefix)) return false;
        final Object this$suffix = this.getSuffix();
        final Object other$suffix = other.getSuffix();
        if (this$suffix == null ? other$suffix != null : !this$suffix.equals(other$suffix)) return false;
        final Object this$nameTagVisibility = this.getNameTagVisibility();
        final Object other$nameTagVisibility = other.getNameTagVisibility();
        if (this$nameTagVisibility == null ? other$nameTagVisibility != null : !this$nameTagVisibility.equals(other$nameTagVisibility))
            return false;
        final Object this$collisionRule = this.getCollisionRule();
        final Object other$collisionRule = other.getCollisionRule();
        if (this$collisionRule == null ? other$collisionRule != null : !this$collisionRule.equals(other$collisionRule))
            return false;
        if (this.getColor() != other.getColor()) return false;
        if (this.getFriendlyFire() != other.getFriendlyFire()) return false;
        return java.util.Arrays.deepEquals(this.getPlayers(), other.getPlayers());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Team;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + this.getMode();
        final Object $displayName = this.getDisplayName();
        result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
        final Object $prefix = this.getPrefix();
        result = result * PRIME + ($prefix == null ? 43 : $prefix.hashCode());
        final Object $suffix = this.getSuffix();
        result = result * PRIME + ($suffix == null ? 43 : $suffix.hashCode());
        final Object $nameTagVisibility = this.getNameTagVisibility();
        result = result * PRIME + ($nameTagVisibility == null ? 43 : $nameTagVisibility.hashCode());
        final Object $collisionRule = this.getCollisionRule();
        result = result * PRIME + ($collisionRule == null ? 43 : $collisionRule.hashCode());
        result = result * PRIME + this.getColor();
        result = result * PRIME + this.getFriendlyFire();
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getPlayers());
        return result;
    }
}
