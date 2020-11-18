package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class TabCompleteRequest extends DefinedPacket {

    private int transactionId;
    private String cursor;
    private boolean assumeCommand;
    private boolean hasPositon;
    private long position;

    public TabCompleteRequest(int transactionId, String cursor) {
        this.transactionId = transactionId;
        this.cursor = cursor;
    }

    public TabCompleteRequest(String cursor, boolean assumeCommand, boolean hasPosition, long position) {
        this.cursor = cursor;
        this.assumeCommand = assumeCommand;
        this.hasPositon = hasPosition;
        this.position = position;
    }

    public TabCompleteRequest() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
            transactionId = readVarInt(buf);
        }
        cursor = readString(buf);

        if (protocolVersion < ProtocolConstants.MINECRAFT_1_13) {
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
                assumeCommand = buf.readBoolean();
            }

            if (hasPositon = buf.readBoolean()) {
                position = buf.readLong();
            }
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
            writeVarInt(transactionId, buf);
        }
        writeString(cursor, buf);

        if (protocolVersion < ProtocolConstants.MINECRAFT_1_13) {
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
                buf.writeBoolean(assumeCommand);
            }

            buf.writeBoolean(hasPositon);
            if (hasPositon) {
                buf.writeLong(position);
            }
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getCursor() {
        return this.cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public boolean isAssumeCommand() {
        return this.assumeCommand;
    }

    public void setAssumeCommand(boolean assumeCommand) {
        this.assumeCommand = assumeCommand;
    }

    public boolean isHasPositon() {
        return this.hasPositon;
    }

    public void setHasPositon(boolean hasPositon) {
        this.hasPositon = hasPositon;
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String toString() {
        return "TabCompleteRequest(transactionId=" + this.transactionId + ", cursor=" + this.cursor + ", assumeCommand=" + this.assumeCommand + ", hasPositon=" + this.hasPositon + ", position=" + this.position + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TabCompleteRequest)) return false;
        final TabCompleteRequest other = (TabCompleteRequest) o;
        if (!other.canEqual(this)) return false;
        if (this.getTransactionId() != other.getTransactionId()) return false;
        final Object this$cursor = this.getCursor();
        final Object other$cursor = other.getCursor();
        if (this$cursor == null ? other$cursor != null : !this$cursor.equals(other$cursor)) return false;
        if (this.isAssumeCommand() != other.isAssumeCommand()) return false;
        if (this.isHasPositon() != other.isHasPositon()) return false;
        return this.getPosition() == other.getPosition();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TabCompleteRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getTransactionId();
        final Object $cursor = this.getCursor();
        result = result * PRIME + ($cursor == null ? 43 : $cursor.hashCode());
        result = result * PRIME + (this.isAssumeCommand() ? 79 : 97);
        result = result * PRIME + (this.isHasPositon() ? 79 : 97);
        final long $position = this.getPosition();
        result = result * PRIME + (int) ($position >>> 32 ^ $position);
        return result;
    }
}
