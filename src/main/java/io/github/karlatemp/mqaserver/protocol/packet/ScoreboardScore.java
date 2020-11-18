package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class ScoreboardScore extends DefinedPacket {

    private String itemName;
    /**
     * 0 = create / update, 1 = remove.
     */
    private byte action;
    private String scoreName;
    private int value;

    public ScoreboardScore(String itemName, byte action, String scoreName, int value) {
        this.itemName = itemName;
        this.action = action;
        this.scoreName = scoreName;
        this.value = value;
    }

    public ScoreboardScore() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        itemName = readString(buf);
        action = buf.readByte();
        scoreName = readString(buf);
        if (action != 1) {
            value = readVarInt(buf);
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(itemName, buf);
        buf.writeByte(action);
        writeString(scoreName, buf);
        if (action != 1) {
            writeVarInt(value, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public byte getAction() {
        return this.action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public String getScoreName() {
        return this.scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return "ScoreboardScore(itemName=" + this.itemName + ", action=" + this.action + ", scoreName=" + this.scoreName + ", value=" + this.value + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ScoreboardScore)) return false;
        final ScoreboardScore other = (ScoreboardScore) o;
        if (!other.canEqual(this)) return false;
        final Object this$itemName = this.getItemName();
        final Object other$itemName = other.getItemName();
        if (this$itemName == null ? other$itemName != null : !this$itemName.equals(other$itemName)) return false;
        if (this.getAction() != other.getAction()) return false;
        final Object this$scoreName = this.getScoreName();
        final Object other$scoreName = other.getScoreName();
        if (this$scoreName == null ? other$scoreName != null : !this$scoreName.equals(other$scoreName)) return false;
        return this.getValue() == other.getValue();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ScoreboardScore;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $itemName = this.getItemName();
        result = result * PRIME + ($itemName == null ? 43 : $itemName.hashCode());
        result = result * PRIME + this.getAction();
        final Object $scoreName = this.getScoreName();
        result = result * PRIME + ($scoreName == null ? 43 : $scoreName.hashCode());
        result = result * PRIME + this.getValue();
        return result;
    }
}
