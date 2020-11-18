package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.utils.LoginResult;
import io.github.karlatemp.mqaserver.utils.Util;
import io.netty.buffer.ByteBuf;

public class PlayerInfo extends DefinedPacket {
    private final LoginResult result;

    public PlayerInfo() {
        this(null);
    }

    public PlayerInfo(LoginResult result) {
        this.result = result;
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(0, buf);
        writeVarInt(1, buf);
        writeUUID(Util.getUUID(result.getId()), buf);
        writeString(result.getName(), buf);
        LoginResult.Property[] properties = result.getProperties();
        writeVarInt(properties.length, buf);
        for (LoginResult.Property property : properties) {
            writeString(property.getName(), buf);
            writeString(property.getValue(), buf);
            String signature = property.getSignature();
            if (signature != null) {
                buf.writeBoolean(true);
                writeString(signature, buf);
            } else {
                buf.writeBoolean(false);
            }
        }
        writeVarInt(2, buf);
        writeVarInt(0, buf);
        buf.writeBoolean(false);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
