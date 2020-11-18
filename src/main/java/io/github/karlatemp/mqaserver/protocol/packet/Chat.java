package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class Chat extends DefinedPacket {

    private static final UUID EMPTY_UUID = new UUID(0L, 0L);
    private String message;
    private byte position;
    private UUID sender;

    public Chat(String message) {
        this(message, (byte) 0);
    }

    public Chat(String message, byte position) {
        this(message, position, EMPTY_UUID);
    }

    public Chat(String message, byte position, UUID sender) {
        this.message = message;
        this.position = position;
        this.sender = sender;
    }

    public Chat() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        message = readString(buf);
        if (direction == ProtocolConstants.Direction.TO_CLIENT) {
            position = buf.readByte();
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
                sender = readUUID(buf);
            }
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(message, buf);
        if (direction == ProtocolConstants.Direction.TO_CLIENT) {
            buf.writeByte(position);
            if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
                writeUUID(sender, buf);
            }
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte getPosition() {
        return this.position;
    }

    public void setPosition(byte position) {
        this.position = position;
    }

    public UUID getSender() {
        return this.sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public String toString() {
        return "Chat(message=" + this.message + ", position=" + this.position + ", sender=" + this.sender + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Chat)) return false;
        final Chat other = (Chat) o;
        if (!other.canEqual(this)) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
        if (this.getPosition() != other.getPosition()) return false;
        final Object this$sender = this.getSender();
        final Object other$sender = other.getSender();
        return this$sender == null ? other$sender == null : this$sender.equals(other$sender);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Chat;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        result = result * PRIME + this.getPosition();
        final Object $sender = this.getSender();
        result = result * PRIME + ($sender == null ? 43 : $sender.hashCode());
        return result;
    }
}
