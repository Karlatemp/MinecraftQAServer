package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.OverflowPacketException;
import io.netty.buffer.ByteBuf;

public class LoginPayloadRequest extends DefinedPacket {

    private int id;
    private String channel;
    private byte[] data;

    public LoginPayloadRequest(int id, String channel, byte[] data) {
        this.id = id;
        this.channel = channel;
        this.data = data;
    }

    public LoginPayloadRequest() {
    }

    @Override
    public void read(ByteBuf buf) {
        id = readVarInt(buf);
        channel = readString(buf);

        int len = buf.readableBytes();
        if (len > 1048576) {
            throw new OverflowPacketException("Payload may not be larger than 1048576 bytes");
        }
        data = new byte[len];
        buf.readBytes(data);
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(id, buf);
        writeString(channel, buf);
        buf.writeBytes(data);
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

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        return "LoginPayloadRequest(id=" + this.id + ", channel=" + this.channel + ", data=" + java.util.Arrays.toString(this.data) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginPayloadRequest)) return false;
        final LoginPayloadRequest other = (LoginPayloadRequest) o;
        if (!other.canEqual(this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$channel = this.getChannel();
        final Object other$channel = other.getChannel();
        if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
        return java.util.Arrays.equals(this.getData(), other.getData());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginPayloadRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $channel = this.getChannel();
        result = result * PRIME + ($channel == null ? 43 : $channel.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getData());
        return result;
    }
}
