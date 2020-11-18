package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.OverflowPacketException;
import io.netty.buffer.ByteBuf;

public class LoginPayloadResponse extends DefinedPacket {

    private int id;
    private byte[] data;

    public LoginPayloadResponse(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public LoginPayloadResponse() {
    }

    @Override
    public void read(ByteBuf buf) {
        id = readVarInt(buf);

        if (buf.readBoolean()) {
            int len = buf.readableBytes();
            if (len > 1048576) {
                throw new OverflowPacketException("Payload may not be larger than 1048576 bytes");
            }
            data = new byte[len];
            buf.readBytes(data);
        }
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(id, buf);
        if (data != null) {
            buf.writeBoolean(true);
            buf.writeBytes(data);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        return "LoginPayloadResponse(id=" + this.id + ", data=" + java.util.Arrays.toString(this.data) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginPayloadResponse)) return false;
        final LoginPayloadResponse other = (LoginPayloadResponse) o;
        if (!other.canEqual(this)) return false;
        if (this.getId() != other.getId()) return false;
        return java.util.Arrays.equals(this.getData(), other.getData());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginPayloadResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        result = result * PRIME + java.util.Arrays.hashCode(this.getData());
        return result;
    }
}
