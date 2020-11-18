package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class ClientSettings extends DefinedPacket {

    private String locale;
    private byte viewDistance;
    private int chatFlags;
    private boolean chatColours;
    private byte difficulty;
    private byte skinParts;
    private int mainHand;

    public ClientSettings(String locale, byte viewDistance, int chatFlags, boolean chatColours, byte difficulty, byte skinParts, int mainHand) {
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatFlags = chatFlags;
        this.chatColours = chatColours;
        this.difficulty = difficulty;
        this.skinParts = skinParts;
        this.mainHand = mainHand;
    }

    public ClientSettings() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        locale = readString(buf);
        viewDistance = buf.readByte();
        chatFlags = protocolVersion >= ProtocolConstants.MINECRAFT_1_9 ? DefinedPacket.readVarInt(buf) : buf.readUnsignedByte();
        chatColours = buf.readBoolean();
        skinParts = buf.readByte();
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
            mainHand = DefinedPacket.readVarInt(buf);
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(locale, buf);
        buf.writeByte(viewDistance);
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
            DefinedPacket.writeVarInt(chatFlags, buf);
        } else {
            buf.writeByte(chatFlags);
        }
        buf.writeBoolean(chatColours);
        buf.writeByte(skinParts);
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
            DefinedPacket.writeVarInt(mainHand, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public byte getViewDistance() {
        return this.viewDistance;
    }

    public void setViewDistance(byte viewDistance) {
        this.viewDistance = viewDistance;
    }

    public int getChatFlags() {
        return this.chatFlags;
    }

    public void setChatFlags(int chatFlags) {
        this.chatFlags = chatFlags;
    }

    public boolean isChatColours() {
        return this.chatColours;
    }

    public void setChatColours(boolean chatColours) {
        this.chatColours = chatColours;
    }

    public byte getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public byte getSkinParts() {
        return this.skinParts;
    }

    public void setSkinParts(byte skinParts) {
        this.skinParts = skinParts;
    }

    public int getMainHand() {
        return this.mainHand;
    }

    public void setMainHand(int mainHand) {
        this.mainHand = mainHand;
    }

    public String toString() {
        return "ClientSettings(locale=" + this.locale + ", viewDistance=" + this.viewDistance + ", chatFlags=" + this.chatFlags + ", chatColours=" + this.chatColours + ", difficulty=" + this.difficulty + ", skinParts=" + this.skinParts + ", mainHand=" + this.mainHand + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClientSettings)) return false;
        final ClientSettings other = (ClientSettings) o;
        if (!other.canEqual(this)) return false;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        if (this.getViewDistance() != other.getViewDistance()) return false;
        if (this.getChatFlags() != other.getChatFlags()) return false;
        if (this.isChatColours() != other.isChatColours()) return false;
        if (this.getDifficulty() != other.getDifficulty()) return false;
        if (this.getSkinParts() != other.getSkinParts()) return false;
        return this.getMainHand() == other.getMainHand();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ClientSettings;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $locale = this.getLocale();
        result = result * PRIME + ($locale == null ? 43 : $locale.hashCode());
        result = result * PRIME + this.getViewDistance();
        result = result * PRIME + this.getChatFlags();
        result = result * PRIME + (this.isChatColours() ? 79 : 97);
        result = result * PRIME + this.getDifficulty();
        result = result * PRIME + this.getSkinParts();
        result = result * PRIME + this.getMainHand();
        return result;
    }
}
