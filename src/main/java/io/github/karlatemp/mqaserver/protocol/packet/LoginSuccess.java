package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class LoginSuccess extends DefinedPacket {

    private UUID uuid;
    private String username;

    public LoginSuccess(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public LoginSuccess() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            uuid = readUUID(buf);
        } else {
            uuid = UUID.fromString(readString(buf));
        }
        username = readString(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            writeUUID(uuid, buf);
        } else {
            writeString(uuid.toString(), buf);
        }
        writeString(username, buf);
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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "LoginSuccess(uuid=" + this.uuid + ", username=" + this.username + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginSuccess)) return false;
        final LoginSuccess other = (LoginSuccess) o;
        if (!other.canEqual(this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        return this$username == null ? other$username == null : this$username.equals(other$username);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginSuccess;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        return result;
    }
}
